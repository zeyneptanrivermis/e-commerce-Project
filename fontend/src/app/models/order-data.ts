
export interface OrderData {
  customerId:         number;
  shippingAddressId:  number;
  itemList:           { productId: number; quantity: number; totalPrice: number }[];
  shippingFee:        number;
}
