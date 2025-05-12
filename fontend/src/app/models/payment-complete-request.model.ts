export interface PaymentCompleteRequest {
  paymentIntentId: string;
  amount: number;
  chargeId?: string; // Optional field
}

