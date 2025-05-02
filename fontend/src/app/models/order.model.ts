export interface Order {
  orderId: number;
  itemList: OrderItem[];
}

export interface OrderItem {
  product: {
    name: string;
    price: number;
  };
  quantity: number;
  price: number;
}
