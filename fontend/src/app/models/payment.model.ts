export interface Payment {
  customerId:  number;
  orderId?:    number;
  amount:      number;
  status:      string;
  paymentDate: Date;
}
