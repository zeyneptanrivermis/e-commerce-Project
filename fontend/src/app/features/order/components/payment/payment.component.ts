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

  async submitPayment(): Promise<void> {
  if (this.paymentForm.invalid) {
    this.paymentForm.markAllAsTouched();
    return;
  }

  this.isLoading = true;
  try {
    // 1) clientSecret al
    const { clientSecret } = await lastValueFrom(
      this.paymentService.createPaymentIntent(this.orderId, this.currency)
    );

    // 2) Kart onayı
    const stripe = await this.stripePromise;
    if (!stripe) throw new Error('Stripe yüklenemedi');

    const result = await stripe.confirmCardPayment(clientSecret, {
      payment_method: {
        card: this.card,
        billing_details: { name: this.paymentForm.value.cardholder }
      }
    });

    if (result.error) throw result.error;

    // 3) Ödeme başarılıysa backend'e sadece intentId ve amount gönder
    if (result.paymentIntent?.status === 'succeeded') {
      // Bu alanı tamamen kaldır:
      const charges = (result.paymentIntent as any).charges;
    let chargeId: string | undefined = undefined;

    if (charges && charges.data && charges.data.length > 0) {
      chargeId = charges.data[0].id;
    }

    if (!chargeId) {
      throw new Error('Charge ID alınamadı.');
    }

      const payload = {
        paymentIntentId: result.paymentIntent.id,
        chargeId: chargeId,
        amount: this.amount
      };

      await lastValueFrom(
        this.paymentService.completePayment(this.orderId, payload)
      );

      // 4) Sepeti temizle ve yönlendir
      this.cartService.clearCart().subscribe(() => {
        alert(`🎉 Ödeme başarıyla tamamlandı! Sipariş #${this.orderId}`);
        this.router.navigate(['/order/history']);
      });
    } else {
      throw new Error('Ödeme onaylanmadı.');
    }
  } catch (err: any) {
    console.error('Ödeme hatası', err);
    alert(err.message || 'Ödeme sırasında bir hata oluştu.');
  } finally {
    this.isLoading = false;
  }
}

}
