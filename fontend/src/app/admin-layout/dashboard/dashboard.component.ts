import { AdminApiService } from './../service/admin-api.service';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../features/auth/services/auth.service';
import { AdminStatsService, Stats } from '../service/admin-stats.service';
import { ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {
  Chart,
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

Chart.register(BarController, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend);

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats?: Stats;
@ViewChild('categoryChart') categoryChartRef!: ElementRef<HTMLCanvasElement>; 


  constructor(
    public authService: AuthService,
    private statsService: AdminStatsService,
    private adminApiService: AdminApiService
  ) {}

  ngOnInit() {
    this.statsService.getStats().subscribe({
      next: (data: Stats) => this.stats = data,
      error: err => console.error('Stats yüklenirken hata:', err)
    });
  }
  ngAfterViewInit() {
  setTimeout(() => {
    const canvas = this.categoryChartRef?.nativeElement;
    if (!canvas) {
      console.warn('Canvas not found');
      return;
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    this.adminApiService.getCategoryProductCounts().subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);

      new Chart(ctx, {
        type: 'bar',
        data: {
          labels,
          datasets: [{
            label: 'Product Count',
            data: values,
            backgroundColor: '#E195AB'
          }]
        },
        options: {
          indexAxis: 'y',
          responsive: true,
          plugins: { legend: { display: false } },
          scales: {
            x: {
              beginAtZero: true,
              title: {
                display: true,
                text: 'Number of Products'
              }
            }
          }
        }});
    });
  }, 0);}
}
