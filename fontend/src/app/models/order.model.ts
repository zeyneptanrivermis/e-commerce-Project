import { OrderItem } from "./order.item.model";
import { Payment } from "./payment.model";

export interface Order {
  orderId?: number;
  totalWithDiscount?: number;
  totalWithoutDiscount?: number;
  status?: string;
  paymentDate?: string; // LocalDate için string (ISO formatı)
  itemList: OrderItem[];
  paymentInfo: Payment;
}
