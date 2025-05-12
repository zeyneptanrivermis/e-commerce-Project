// src/app/components/payment/payment.component.ts
import {
  Component,
  OnInit,
  ViewChild,
  ElementRef
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  loadStripe,
  Stripe,
  StripeCardElement,
  StripeElements
} from '@stripe/stripe-js';

import { PaymentService } from '../../service/payment.service';
import { CartService } from '../../../cart/services/cart.service';
import { lastValueFrom } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'app-payment',
  standalone: false,
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  @ViewChild('cardInfo', { static: true }) cardInfo!: ElementRef;

  paymentForm!: FormGroup;
  private stripePromise!: Promise<Stripe | null>;
  private elements!: StripeElements;
  private card!: StripeCardElement;

  orderId!: number;
  amount!: number;
  currency = 'try';
  isLoading = false;
  loadingMessage = '';

  // Minimum loading time in milliseconds
  private readonly MIN_LOADING_TIME = 2000;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.paymentForm = this.fb.group({
      amount:     [{ value: 0, disabled: true }, Validators.required],
      cardholder: ['', Validators.required],
    });

    this.route.queryParams.subscribe(params => {
      this.orderId = Number(params['orderId']);
      this.amount  = Number(params['amount']) || 0;
      this.paymentForm.get('amount')!.setValue(this.amount);
    });

    this.stripePromise = loadStripe(environment.stripePublicKey);
    this.stripePromise.then(stripe => {
      if (!stripe) {
        console.error('Stripe yüklenemedi');
        return;
      }
      this.elements = stripe.elements({ locale: 'tr' });
      this.card = this.elements.create('card');
      this.card.mount(this.cardInfo.nativeElement);
    });
  }

  // Helper method to handle loading state with minimum duration
  private async withLoadingState<T>(operation: () => Promise<T>, message: string): Promise<T> {
    const startTime = Date.now();
    this.isLoading = true;
    this.loadingMessage = message;

    try {
      const result = await operation();
      const elapsedTime = Date.now() - startTime;
      const remainingTime = Math.max(0, this.MIN_LOADING_TIME - elapsedTime);
      
      if (remainingTime > 0) {
        await new Promise(resolve => setTimeout(resolve, remainingTime));
      }
      
      return result;
    } finally {
      this.isLoading = false;
      this.loadingMessage = '';
    }
  }

  async submitPayment(): Promise<void> {
    if (this.paymentForm.invalid) {
      this.paymentForm.markAllAsTouched();
      return;
    }

    try {
      // 1) clientSecret al
      const { clientSecret } = await this.withLoadingState(
        async () => {
          const result = await lastValueFrom(
            this.paymentService.createPaymentIntent(this.orderId, this.currency)
          ).catch(error => {
            if (error.status === 400) {
              if (error.error?.error?.includes('zaten tamamlanmış')) {
                throw new Error('Bu sipariş için ödeme zaten tamamlanmış. Sipariş geçmişinizi kontrol edebilirsiniz.');
              }
              if (error.error?.error?.includes('Minimum ödeme tutarı')) {
                throw new Error('Sepet tutarınız çok düşük. Lütfen sepetinize daha fazla ürün ekleyin veya farklı ürünler seçin.');
              }
            }
            throw new Error('Ödeme başlatılırken bir hata oluştu: ' + (error.error?.error || error.message));
          });
          return result;
        },
        'Ödeme işlemi başlatılıyor...'
      );

      // 2) Kart onayı
      const stripe = await this.stripePromise;
      if (!stripe) throw new Error('Stripe yüklenemedi');

      const result = await this.withLoadingState(
        async () => {
          return await stripe.confirmCardPayment(clientSecret, {
            payment_method: {
              card: this.card,
              billing_details: { name: this.paymentForm.value.cardholder }
            }
          });
        },
        'Kart bilgileri doğrulanıyor...'
      );

      if (result.error) {
        if (result.error.type === 'card_error' || result.error.type === 'validation_error') {
          throw new Error(result.error.message);
        }
        throw new Error('Ödeme işlemi sırasında bir hata oluştu: ' + result.error.message);
      }

      // 3) Ödeme başarılıysa backend'e intentId, chargeId ve amount gönder
      if (result.paymentIntent?.status === 'succeeded') {
        const paymentIntent = result.paymentIntent as any;
        const chargeId = paymentIntent.latest_charge || paymentIntent.id;
        
        const payload = {
          paymentIntentId: result.paymentIntent.id,
          chargeId: chargeId,
          amount: this.amount
        };

        await this.withLoadingState(
          async () => {
            await lastValueFrom(
              this.paymentService.completePayment(this.orderId, payload)
            ).catch(error => {
              throw new Error('Ödeme tamamlanırken bir hata oluştu: ' + (error.error?.error || error.message));
            });
          },
          'Ödeme tamamlanıyor...'
        );

        // 4) Sepeti temizle ve yönlendir
        await this.withLoadingState(
          async () => {
            return new Promise<void>((resolve, reject) => {
              this.cartService.clearCart().subscribe({
                next: () => {
                  alert(`🎉 Ödeme başarıyla tamamlandı! Sipariş #${this.orderId}`);
                  this.router.navigate(['/order/history']);
                  resolve();
                },
                error: (error) => {
                  console.error('Sepet temizlenirken hata:', error);
                  alert(`🎉 Ödeme başarıyla tamamlandı! Sipariş #${this.orderId}\nNot: Sepet temizlenirken bir hata oluştu.`);
                  this.router.navigate(['/order/history']);
                  resolve(); // Still resolve since payment was successful
                }
              });
            });
          },
          'Sipariş tamamlanıyor...'
        );
      } else {
        throw new Error('Ödeme onaylanmadı. Lütfen tekrar deneyin.');
      }
    } catch (err: any) {
      console.error('Ödeme hatası', err);
      alert(err.message || 'Ödeme sırasında bir hata oluştu. Lütfen tekrar deneyin.');
    }
  }
}
