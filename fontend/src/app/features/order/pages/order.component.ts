import { Component, OnInit } from '@angular/core';
import { OrderService } from '../service/order.service';
import { Order } from '../../../models/order.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrderItem } from '../../../models/order.item.model';
import { CartService } from '../../cart/services/cart.service';
import { AuthService } from '../../auth/services/auth.service';
import { AddressService } from '../../address/service/address.service';
import { Address } from '../../../models/Address.model';

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

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private cartService: CartService,
    private authService: AuthService,
    private addressService: AddressService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadAddresses();
    this.loadCart();
    const user = this.authService.getCurrentUser();
    this.userId = user?.userId ?? 0;
      if (user) {
        this.addressService.getUserAddresses(user.userId).subscribe({
          next: (addresses) => this.addresses = addresses,
          error: (err) => console.error('Adresler alınamadı:', err)
        });
      }

  }

  initForm(): void {
    this.addressForm = this.fb.group({
      selectedAddressId: this.selectedAddressId,
      country: ['Turkey', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      addressDetail: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  loadCart(): void {
    this.cartService.listCartItems().subscribe({
      next: (items) => {
        this.cartItems = items.map(item => ({
          productId: item.product.productId,
          quantity: item.quantity,
          price: item.product.price
        }));
      },
      error: (err) => console.error('Sepet verisi alınamadı', err)
    });
  }

  loadAddresses(): void {
    const user = this.authService.getCurrentUser();
    const userId = user?.userId ?? 0;

    this.addressService.getUserAddresses(userId).subscribe({
      next: (addresses: Address[]) => {
        this.addresses = addresses;
        this.selectedAddressId = addresses[0]?.addressId;
      },
      error: (err) => console.error('Adresler alınamadı', err)
    });
  }



  toggleAddressForm(): void {
    this.showAddressForm = !this.showAddressForm;
  }

  saveNewAddress(): void {
    const address: Address = this.addressForm.value;
    this.addressService.addAddress(this.userId, address).subscribe({
      next: () => {
        this.loadAddresses();
        this.toggleAddressForm();
      },
      error: err => console.error('Adres eklenemedi:', err)
    });
    
  }

  getCartTotal(): number {
    return this.cartItems.reduce((total, item) => total + (item.quantity * item.price), 0);
  }

  placeOrder(): void {
    if (!this.selectedAddressId || this.cartItems.length === 0) return;

    const order: Order = {
      customerId: this.userId,
      itemList: this.cartItems,
      shippingAddressId: this.selectedAddressId
    };

    // Burada 'POST' metodunu kullandığından emin ol
    this.orderService.placeOrder(order).subscribe({
      next: () => {
        alert('Sipariş oluşturuldu!');
        this.clearCartManually();
      },
      error: err => {
        console.error(err);
        alert('Sipariş oluşturulamadı.');
      }
    });
  }

  clearCartManually(): void {
    this.cartItems.forEach(item => {
      this.cartService.removeFromCart(item.productId).subscribe({
        next: () => {},
        error: err => console.error('Silinemedi:', err)
      });
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

  resetForm(): void {
    this.addressForm.reset({
      country: 'Turkey',
      city: '',
      district: '',
      addressDetail: ''
    });
  }

}
