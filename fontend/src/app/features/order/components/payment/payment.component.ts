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
  StripeElements,
  StripeElementsOptions
} from '@stripe/stripe-js';

import { PaymentService } from '../../service/payment.service';
import { CartService } from '../../../cart/services/cart.service';
import { firstValueFrom } from 'rxjs';
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

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Formu hazırla
    this.paymentForm = this.fb.group({
      amount:     [{ value: 0, disabled: true }, Validators.required],
      cardholder: ['', Validators.required],
    });

    // Query param’leri al
    this.route.queryParams.subscribe(params => {
      this.orderId       = Number(params['orderId']);
      this.amount  = Number(params['amount'])  || 0;
      this.paymentForm.get('amount')!.setValue(this.amount);
    });

    // Stripe Elements’i yükle ve mount et
    this.stripePromise = loadStripe(environment.stripePublicKey);
    this.stripePromise.then((stripe: Stripe | null) => {
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

  try {
    // 1) İlk değer olarak clientSecret’i alıyoruz.
    const { clientSecret } = await firstValueFrom(
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

    // 3) Başarılıysa backend’e bildir
    if (result.paymentIntent?.status === 'succeeded') {
      await firstValueFrom(
        this.paymentService.completePayment(this.orderId, result.paymentIntent.id)
      );

      // 4) Sepeti temizle ve yönlendir
      this.cartService.clearCart().subscribe(() => {
        alert(`🎉 Ödeme başarıyla tamamlandı! Sipariş #${this.orderId}`);
        this.router.navigate(['/order/history']);
      });
    }
  } catch (err: any) {
    console.error('Ödeme hatası', err);
    alert(err.message || 'Ödeme sırasında bir hata oluştu.');
  }
  }
}
