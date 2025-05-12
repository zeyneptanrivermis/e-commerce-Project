import { Component, OnInit } from '@angular/core';
import { SellerOrderService } from '../../services/seller-order.service';

@Component({
  selector: 'app-seller-orders',
  standalone: false,
  templateUrl: './seller-orders.component.html',
  styleUrl: './seller-orders.component.css'
})
export class SellerOrdersComponent implements OnInit {
  orders: any[] = [];

  constructor(private sellerOrderService: SellerOrderService) {}

  ngOnInit(): void {
    this.sellerOrderService.getSellerOrders().subscribe({
      next: (orders) => {
        this.orders = orders;
      },
      error: (err) => {
        console.error('Failed to load seller orders:', err);
      }
    });
  }
}
