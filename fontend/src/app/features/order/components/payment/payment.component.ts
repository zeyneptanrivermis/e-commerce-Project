import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../service/payment.service';
import { OrderService } from '../../service/order.service';
import { CartService } from '../../../cart/services/cart.service';
import { OrderItem } from '../../../../models/order.item.model';

@Component({
  selector: 'app-payment',
  standalone: false,
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  paymentForm!: FormGroup;
  orderId!: number;
  itemsTotal = 0;
  shippingFee = 0;
  amount = 0;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // 1) Build the form immediately
    this.initForm();

    // 2) Read values from queryParams
    this.route.queryParams.subscribe(params => {
      this.itemsTotal  = +params['itemsTotal']  || 0;
      this.shippingFee = +params['shippingFee'] || 0;
      this.amount      = +params['amount']      || (this.itemsTotal + this.shippingFee);
      this.orderId     = +params['orderId']     || 0;

      // 3) Patch the amount into the form control
      this.paymentForm.get('amount')!.setValue(this.amount);
    });
  }

  private initForm(): void {
    this.paymentForm = this.fb.group({
      amount:      [{ value: 0, disabled: true }, Validators.required],
      cardholder:  ['', Validators.required],
      cardNumber:  ['', [Validators.required, Validators.minLength(16)]],
      expiryMonth: ['', [Validators.required, Validators.pattern('^(0[1-9]|1[0-2])$')]],
      expiryYear:  ['', [Validators.required, Validators.pattern('^\\d{2}$')]],
      cvv:         ['', [Validators.required, Validators.minLength(3), Validators.maxLength(4)]]
    });
  }

  submitPayment(): void {
    if (this.paymentForm.invalid) {
      this.paymentForm.markAllAsTouched();
      return;
    }

    const amount = this.paymentForm.get('amount')!.value as number;
    this.paymentService.addPayment(this.orderId, amount).subscribe({
      next: () => {
        this.cartService.clearCart().subscribe(() => {
          alert(`🎉 Payment successful! Order #${this.orderId}`);
          this.router.navigate(['/order', 'history']);
        });
      },
      error: err => {
        console.error('Payment error', err);
        alert('❌ Payment failed. Please try again.');
      }
    });
  }
}
