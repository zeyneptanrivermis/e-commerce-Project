export interface Payment {
  customerId:  number;
  orderId:     number;
  amount:      number;
  status:      string;
  cardholder:  string;
  cardNumber:  string;
  expiryMonth: string;
  expiryYear:  string;
  cvv:         string;
}
