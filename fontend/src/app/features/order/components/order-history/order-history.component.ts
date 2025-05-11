import { Component, OnInit } from '@angular/core';
import { OrderHistory } from '../../../../models/OrderHistory.model';
import { OrderHistoryService } from '../../service/order-history.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-history',
  standalone: false,
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent implements OnInit {
  orders: OrderHistory[] = [];
  isLoading = true;

  constructor(
    private historyService: OrderHistoryService,
    private router: Router                                     // ← EKLE
  ) {}

  ngOnInit(): void {
    this.historyService.getHistory().subscribe({
      next: data => {
        this.orders = data;
        this.isLoading = false;
      },
      error: err => {
        console.error('History load error', err);
        alert('Sipariş geçmişi yüklenemedi');
        this.isLoading = false;
      }
    });
  }

  /** ID tıklanınca yönlendir */
  goToShipment(orderId: number): void {
    this.router.navigate(['/shipment', orderId]);
  }
}
