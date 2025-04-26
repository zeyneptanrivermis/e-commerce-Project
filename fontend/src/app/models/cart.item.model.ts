import { Product } from './product.model'; // Eğer Product modelin varsa import edebilirsin

export interface CartItem {
  cartItemId: number;
  product: Product;
  quantity: number;
  totalPrice: number;
}
