import { Component } from '@angular/core';
import { AdminStatsService } from '../service/admin-stats.service';
import { AuthService } from '../../features/auth/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  stats?: { userCount: number, productCount: number, pendingOrders: number };

  // ① AuthService'i burada tanımlayıp inject ediyoruz
  constructor(
    public authService: AuthService,
    private statsService: AdminStatsService
  ) {}

  ngOnInit() {
    // ② Kullanıcı adı gibi bilgilere authService üzerinden erişebilirsiniz
    console.log('Hoş geldin:', this.authService.getCurrentUser?.name);

    this.statsService.getStats().subscribe(data => this.stats = data);
  }
}
