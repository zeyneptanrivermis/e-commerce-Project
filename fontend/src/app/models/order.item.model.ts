// order.item.model.ts
export interface OrderItem {
  orderItemId?: number;
  productId:   number;
  productName?: string;    // isterseniz ekleyin
  quantity:    number;
  totalPrice:  number;     // back-end’den gelen toplam
}
