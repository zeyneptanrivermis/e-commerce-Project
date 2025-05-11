import { Component, OnInit } from '@angular/core';
import { AdminApiService } from '../service/admin-api.service';
import { Order } from '../../models/order.model';
import { ViewChild, ElementRef, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-orders',
  standalone: false,
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {
  orders: Order[] = [];
  selectedOrder: any = null;
  @ViewChild('pieChart', { static: true }) pieChartRef!: ElementRef<HTMLCanvasElement>;

  constructor(private api: AdminApiService) {}

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.api.getAllOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.drawPieChart();
      },
      error: (err) => console.error('Failed to load orders:', err)
    });
  }

  viewOrderDetails(order: any) {
    this.selectedOrder = order;
  }

  closeDetails() {
    this.selectedOrder = null;
  }

  changeStatus(order: Order, newStatus: string) {
    this.api.updateOrderStatus(order.orderId, newStatus).subscribe({
      next: (updatedOrder) => {
        order.status = updatedOrder.status;
        this.drawPieChart();
      },
      error: (err) => {
        console.error('Failed to update status:', err);
        // Optionally show a user-friendly error
      }
    });
  }

  drawPieChart() {
    if (!this.pieChartRef) return;
    const canvas = this.pieChartRef.nativeElement;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;
  
    // Count statuses
    const pending = this.orders.filter(o => o.status === 'PENDING').length;
    const accepted = this.orders.filter(o => o.status === 'COMPLETED').length;
    const cancelled = this.orders.filter(o => o.status === 'CANCELLED').length;
    const data = [pending, accepted, cancelled];
    const colors = ['#fbbf24', '#34d399', '#f87171'];
  
    // Clear canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);
  
    // Draw pie
    const total = data.reduce((a, b) => a + b, 0) || 1;
    let startAngle = -0.5 * Math.PI;
    for (let i = 0; i < data.length; i++) {
      const sliceAngle = (data[i] / total) * 2 * Math.PI;
      ctx.beginPath();
      ctx.moveTo(90, 90);
      ctx.arc(90, 90, 80, startAngle, startAngle + sliceAngle);
      ctx.closePath();
      ctx.fillStyle = colors[i];
      ctx.fill();
      startAngle += sliceAngle;
    }
  }
}
