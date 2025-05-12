import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../features/order/service/order.service';
import { AdminRefundService } from '../service/AdminRefund.service';
import { RefundResponse } from '../../models/RefundResponse.model';


interface OrderDTO {
  orderId: number;
  customerName: string;
  totalWithDiscount: number;
  status: string;
  paymentDate: string;
}

@Component({
  selector: 'app-RefundList',
  standalone: false,
  templateUrl: './RefundList.component.html',
  styleUrls: ['./RefundList.component.css']
})
export class RefundListComponent implements OnInit {
  pending: OrderDTO[] = [];
  isLoading = false;
  error = '';

  constructor(
    private orderService: OrderService,
    private adminRefund: AdminRefundService
  ) {}

  ngOnInit(): void {
    this.loadPending();
  }

  loadPending(): void {
    this.isLoading = true;
    this.error = '';
    this.orderService.getOrders().subscribe({
      next: orders => {
        this.pending = orders.filter(o => o.status === 'REFUND_REQUESTED');
        this.isLoading = false;
      },
      error: err => {
        this.error = 'Bekleyen iadeler yüklenemedi.';
        this.isLoading = false;
      }
    });
  }

  approve(orderId: number): void {
    this.adminRefund.approve(orderId).subscribe({
      next: (res: RefundResponse) => {
        alert('İade onaylandı: ' + res.refundId);
        this.loadPending();
      },
      error: () => alert('İade onayı sırasında hata oluştu.')
    });
  }

  decline(orderId: number): void {
    if (!confirm('İade talebini reddetmek istediğinize emin misiniz?')) return;
    this.adminRefund.decline(orderId).subscribe({
      next: () => {
        alert('İade talebi reddedildi.');
        this.loadPending();
      },
      error: () => alert('İade reddi sırasında hata oluştu.')
    });
  }
}
