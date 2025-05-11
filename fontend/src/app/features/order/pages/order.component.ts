import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';                   // ← eklendi
import { CartService } from '../../cart/services/cart.service';
import { AuthService } from '../../auth/services/auth.service';
import { AddressService } from '../../address/service/address.service';
import { Address } from '../../../models/Address.model';
import { OrderItem } from '../../../models/order.item.model';
import { OrderService } from '../service/order.service';
import { OrderData } from '../../../models/order-data';

@Component({
  selector: 'app-order',
  standalone: false,
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})

export class OrderComponent implements OnInit {
  addressForm!: FormGroup;
  cartItems: OrderItem[] = [];
  addresses: Address[] = [];
  selectedAddressId!: number;
  userId!: number;
  showAddressForm = false;
  shippingFee = 44.99;
  totalWithoutDiscount = 0;
  orderId!: number;

  constructor(
    private fb: FormBuilder,
    private cartService: CartService,
    private authService: AuthService,
    private addressService: AddressService,
    private orderService: OrderService,
    private router: Router                    // ← eklendi
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    this.userId = user?.userId ?? 0;

    this.initForm();
    this.loadAddresses();
    this.loadCart();
  }

  private initForm(): void {
    this.addressForm = this.fb.group({
      selectedAddressId: [null, Validators.required],
      country:          ['Turkey', Validators.required],
      city:             ['', Validators.required],
      district:         ['', Validators.required],
      addressDetail:    ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  private loadCart(): void {
    this.cartService.listCartItems().subscribe({
      next: items => {
        this.cartItems = items.map(item => ({
          productId:   item.product.productId,
          productName: item.product.name,
          quantity:    item.quantity,
          totalPrice:  item.totalPrice
        }));
        this.totalWithoutDiscount = this.cartItems
          .reduce((sum, i) => sum + i.totalPrice, 0);
      },
      error: err => console.error('Sepet verisi alınamadı', err)
    });
  }

  private loadAddresses(): void {
    this.addressService
      .getUserAddresses(this.userId)
      .subscribe({
        next: addrs => {
          this.addresses = addrs;
          this.selectedAddressId = addrs[0]?.addressId;
        },
        error: err => console.error('Adresler alınamadı', err)
      });
  }

  toggleAddressForm(): void {
    this.showAddressForm = !this.showAddressForm;
  }

  saveNewAddress(): void {
    if (this.addressForm.invalid) {
      this.addressForm.markAllAsTouched();
      return;
    }
    const address: Address = this.addressForm.value;
    this.addressService.addAddress(this.userId, address)
      .subscribe({
        next: () => {
          this.loadAddresses();
          this.toggleAddressForm();
        },
        error: err => console.error('Adres eklenemedi', err)
      });
  }

  goToPayment(): void {
    if (this.cartItems.length === 0) {
      alert('Önce sepete ürün ekleyin.');
      return;
    }

    const orderData: OrderData = {
      customerId:        this.userId,
      shippingAddressId: this.selectedAddressId,
      itemList:          this.cartItems.map(i => ({
        productId:  i.productId,
        quantity:   i.quantity,
        totalPrice: i.totalPrice
      })),
      shippingFee: this.shippingFee
    };

    this.orderService.createOrder().subscribe({
      next: order => {
        const id = order.orderId!;
        const amount = Math.round((this.totalWithoutDiscount + this.shippingFee) * 100);
        this.router.navigate(['/order','payment'], {
          queryParams: { orderId: id, amount }
        });
      },
      error: err => {
        console.error('Order couldnt be processed', err);
        alert('Sipariş oluşturma sırasında hata oluştu.');
      }
    });
  }

  resetForm(): void {
    this.addressForm.reset({
      country: 'Turkey',
      city: '',
      district: '',
      addressDetail: ''
    });
  }

  saveAddress(): void {
    if (this.addressForm.invalid) {
      this.addressForm.markAllAsTouched();
      return;
    }

    const address: Address = this.addressForm.value;

    this.addressService.addAddress(this.userId, address).subscribe({
      next: () => {
        this.loadAddresses();       // Adres listesini yeniden yükle
        this.resetForm();           // Formu temizle
        this.showAddressForm = false; // Formu gizle
      },
      error: err => {
        console.error('Adres eklenemedi:', err);
        alert('Adres kaydedilirken hata oluştu.');
      }
    });
  }
}
