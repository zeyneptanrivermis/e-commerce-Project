import { User } from './../../../models/user.model';
import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../order/service/order.service';
import { Order } from '../../../models/order.model';
import { AuthService } from '../../auth/services/auth.service';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-user-orderlist',
  templateUrl: './user-orderlist.component.html',
  styleUrls: ['./user-orderlist.component.css'],
  standalone:false
})
export class UserOrderlistComponent implements OnInit {

  orders: Order[] = [];
  loading: boolean = false;
  error: string = '';

  constructor(
    private orderService: OrderService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.orderService.getUserOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Siparişler alınamadı';
        this.loading = false;
      }
    });
  }
  
}
