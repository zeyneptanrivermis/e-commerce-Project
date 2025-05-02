import { Component, OnInit } from '@angular/core';
import { OrderService } from '../service/order.service';
import { Order } from '../../../models/order.model';


@Component({
  selector: 'app-order',
  standalone: false,
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  orders: Order[] = [];
  isLoading = false;
  errorMessage: string = '';

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.isLoading = true;
    this.orderService.getCustomerOrders().subscribe({
      next: (orders) => {
        this.orders = orders;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load orders.';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  createOrder(): void {
    this.isLoading = true;
    this.orderService.createOrder().subscribe({
      next: (newOrder) => {
        this.orders.unshift(newOrder);
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to create order.';
        console.error(err);
        this.isLoading = false;
      }
    });
  }
}
