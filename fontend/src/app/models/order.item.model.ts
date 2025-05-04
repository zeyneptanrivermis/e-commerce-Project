// order.item.model.ts
export interface OrderItem {
  orderItemId?: number;
  productId: number;
  productName: string;
  quantity: number;
  totalPrice: number;
}