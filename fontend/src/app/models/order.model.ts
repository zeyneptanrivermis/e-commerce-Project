import { OrderItem } from "./order.item.model";

export interface Order {
  orderId: number;
  customerName: string;
  total: number;
  totalWithDiscount: number;
  totalWithoutDiscount: number;
  status: string;
  createdAt: string;
  paymentDate: string;
  itemList: OrderItem[];
  paymentInfo?: {
    status: string;
    paymentDate: string;
  };
}
