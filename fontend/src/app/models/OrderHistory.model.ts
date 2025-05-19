import { OrderItem } from './order.item.model';

export interface OrderHistory {
  orderId: number;
  paymentDate: string;
  status: string;
  totalWithDiscount: number;
  itemList: OrderItem[];
}
