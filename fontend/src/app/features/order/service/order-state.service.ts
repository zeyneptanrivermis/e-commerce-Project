import { Injectable } from '@angular/core';
import { OrderData } from '../../../models/order-data';

@Injectable({
  providedIn: 'root'
})
export class OrderStateService {

  private data!: OrderData;

  /** Sepet–adres adımından sonra çağırın */
  setOrderData(data: OrderData): void {
    this.data = data;
  }

  /** PaymentComponent’te kullanmak için */
  getOrderData(): OrderData {
    if (!this.data) {
      throw new Error('Order data not set');
    }
    return this.data;
  }

  /** İsterseniz temizlemek için */
  clear(): void {
    // @ts-ignore
    this.data = undefined;
  }
}
