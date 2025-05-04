export interface Payment {
  customerId:  number;
  orderId?:     number;
  amount:      number;
  status:      string;
  paymentDate: Date;
  cardholder:  string;
  cardNumber:  string;
  expiryMonth: string;
  expiryYear:  string;
  cvv:         string;
}
