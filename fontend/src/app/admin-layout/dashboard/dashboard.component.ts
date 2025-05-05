import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../features/auth/services/auth.service';
import { AdminStatsService, Stats } from '../service/admin-stats.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats?: Stats;

  constructor(
    public authService: AuthService,
    private statsService: AdminStatsService
  ) {}

  ngOnInit() {
    this.statsService.getStats().subscribe({
      next: (data: Stats) => this.stats = data,
      error: err => console.error('Stats yüklenirken hata:', err)
    });
  }
}
