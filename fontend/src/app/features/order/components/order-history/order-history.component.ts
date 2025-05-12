import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { OrderHistory } from '../../../../models/OrderHistory.model';
import { OrderHistoryService } from '../../service/order-history.service';
import { RefundServiceService } from '../../service/RefundService.service';

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
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  /** Sipariş listesini veya geçmişini yükler */
  loadOrders(): void {
    this.isLoading = true;
    this.error = '';
    this.historyService.getHistory().subscribe({
      next: (data: OrderHistory[]) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Siparişler yüklenemedi:', err.message);
        this.error = err.error?.message || 'Siparişler yüklenemedi.';
        this.isLoading = false;
      }
    });
  }

  /** ID tıklanınca yönlendir */
  goToShipment(orderId: number): void {
    this.router.navigate(['/shipment', orderId]);
  }

  /** Listeyi yenilemek istediğimizde de aynı metodu çağırıyoruz */
  fetchOrders(): void {
    this.loadOrders();
  }

  /** trackBy fonksiyonu performans için */
  trackByOrderId(index: number, item: OrderHistory): number {
    return item.orderId;
  }

  /** Siparişe tıklandığında kargo detayına yönlendir */
  goToShipment(orderId: number): void {
    this.router.navigate(['/shipment', orderId]);
  }

  /** İade onayı al ve işlemi başlat */
  confirmRefund(orderId: number): void {
  if (!confirm('Bu sipariş için iade talep edilsin mi?')) return;
this.refundService.requestRefund(orderId).subscribe();
}

  /** Backend'e iade talebi gönderir ve listeyi yeniler */
  private refundOrder(orderId: number): void {
    this.isLoading = true;
    this.refundService.requestRefund(orderId).subscribe({
      next: () => {
        alert('İade talebiniz alındı.');
        this.loadOrders();
      },
      error: (err: HttpErrorResponse) => {
        console.error('İade hatası:', err.message);
        alert('İade işlemi başarısız: ' + (err.error?.message || err.message));
        this.isLoading = false;
      }
    });
  }
}
