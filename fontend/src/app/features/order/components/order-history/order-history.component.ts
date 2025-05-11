import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderHistory } from '../../../../models/OrderHistory.model';
import { OrderHistoryService } from '../../service/order-history.service';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css'],
  standalone: false
})
export class OrderHistoryComponent implements OnInit {

  orders: OrderHistory[] = [];
  isLoading: boolean = true;
  error: string = '';

  constructor(
    private historyService: OrderHistoryService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadHistory();
  }

  /** Sipariş geçmişini yükle */
  loadHistory(): void {
    this.isLoading = true;
    this.error = '';
    this.historyService.getHistory().subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Sipariş geçmişi yüklenemedi:', err);
        this.error = 'Sipariş geçmişi yüklenemedi.';
        this.isLoading = false;
      }
    });
  }

  /** Siparişleri tazele (örn: iade sonrası) */
  fetchOrders(): void {
    this.isLoading = true;
    this.error = '';
    this.orderService.getOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Siparişler yüklenemedi:', err);
        this.error = 'Siparişler yüklenemedi.';
        this.isLoading = false;
      }
    });
  }

  /** Siparişe tıklandığında kargo detayına yönlendir */
  goToShipment(orderId: number): void {
    this.router.navigate(['/shipment', orderId]);
  }

  /** İade onayı al ve işlemi başlat */
  confirmRefund(orderId: number): void {
    const confirmed = confirm("İade talebinde bulunmak istediğinize emin misiniz?");
    if (confirmed) {
      this.refundOrder(orderId);
    }
  }

  /** Backend'e iade talebi gönder */
  refundOrder(orderId: number): void {
    this.orderService.refundOrder(orderId).subscribe({
      next: () => {
        alert("İade talebiniz alındı.");
        this.fetchOrders(); // Listeyi yenile
      },
      error: (err) => {
        console.error("İade hatası:", err);
        alert("İade işlemi başarısız: " + err.error?.message || 'Sunucu hatası');
      }
    });
  }
}
