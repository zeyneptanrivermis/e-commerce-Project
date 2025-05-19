import { Component, OnInit } from '@angular/core';
import { SellerService, SellerDashboardDTO, TopSellingProduct } from '../../service/seller.service';
import { TokenService } from '../../../core/services/token.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-seller-dashboard',
  templateUrl: './seller-dashboard.component.html',
  styleUrls: ['./seller-dashboard.component.css'],
  standalone: false
})
export class SellerDashboardComponent implements OnInit {

  sellerName: string = '';
  email: string = '';
  totalProducts: number = 0;
  totalOrders: number = 0;
  totalSales: number = 0;
  topSellingProducts: TopSellingProduct[] = [];

  constructor(
    private sellerService: SellerService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.sellerService.getDashboardStats().subscribe({
      next: (data) => {
        this.sellerName = data.name;
        this.email = data.email;
        this.totalProducts = data.totalProducts;
        this.totalOrders = data.totalOrders;
        this.totalSales = data.totalSales;
        this.topSellingProducts = data.topSellingProducts || [];
      },
      error: (err) => {
        console.error('Dashboard loading error:', err);
      }
    });
  }

  logout(): void {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }
}
