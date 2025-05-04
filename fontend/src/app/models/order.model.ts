import { OrderItem } from "./order.item.model";
import { Payment } from "./payment.model";

export interface Order {
  orderId?: number;
  customerId: number;              // Customer objesi yerine sadece ID göndermek yeterli
  itemList: OrderItem[];           // Siparişe ait ürünler
  discountId?: number;             // Varsa indirim uygulanır
  totalWithoutDiscount?: number;   // Backend hesaplıyor
  totalWithDiscount?: number;      // Backend hesaplıyor
  shippingAddressId: number;
  paymentInfo: Payment;
}
