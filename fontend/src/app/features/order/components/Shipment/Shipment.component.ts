import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import { takeWhile } from 'rxjs/operators';

@Component({
  selector: 'app-Shipment',
  standalone: false,
  templateUrl: './Shipment.component.html',
  styleUrls: ['./Shipment.component.css']
})
export class ShipmentComponent implements OnInit, OnDestroy {
  orderId!: number;
  steps = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED'];
  currentStatus = 'PENDING';
  progressPercent = 0;
  private timerSubscription?: Subscription;
  private readonly STEP_DURATION = 10000; // 10 seconds in milliseconds
  private readonly STORAGE_KEY = 'shipment_timer_state_';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('orderId'));
    this.initializeTimer();
  }

  ngOnDestroy(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  private initializeTimer(): void {
    const storageKey = this.STORAGE_KEY + this.orderId;
    const savedState = localStorage.getItem(storageKey);
    
    if (savedState) {
      const state = JSON.parse(savedState);
      this.currentStatus = state.currentStatus;
      this.progressPercent = state.progressPercent;
      
      // If not completed, continue the timer
      if (this.currentStatus !== this.steps[this.steps.length - 1]) {
        this.startTimerFromCurrentState();
      }
    } else {
      // Start new timer
      this.startNewTimer();
    }
  }

  private startNewTimer(): void {
    this.currentStatus = this.steps[0];
    this.progressPercent = 0;
    this.saveState();
    this.startTimer();
  }

  private startTimerFromCurrentState(): void {
    const currentIndex = this.steps.indexOf(this.currentStatus);
    if (currentIndex < this.steps.length - 1) {
      this.startTimer(currentIndex);
    }
  }

  private startTimer(startIndex: number = 0): void {
    this.timerSubscription = interval(this.STEP_DURATION)
      .pipe(
        takeWhile(() => {
          const currentIndex = this.steps.indexOf(this.currentStatus);
          return currentIndex < this.steps.length - 1;
        })
      )
      .subscribe(() => {
        const currentIndex = this.steps.indexOf(this.currentStatus);
        if (currentIndex < this.steps.length - 1) {
          this.currentStatus = this.steps[currentIndex + 1];
          this.progressPercent = ((currentIndex + 1) / (this.steps.length - 1)) * 100;
          this.saveState();
        }
      });
  }

  private saveState(): void {
    const storageKey = this.STORAGE_KEY + this.orderId;
    const state = {
      currentStatus: this.currentStatus,
      progressPercent: this.progressPercent
    };
    localStorage.setItem(storageKey, JSON.stringify(state));
  }
}
