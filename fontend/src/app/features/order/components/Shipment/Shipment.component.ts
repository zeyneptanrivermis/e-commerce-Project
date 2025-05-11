import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ShipmentService } from '../../service/Shipment.service';

@Component({
  selector: 'app-Shipment',
  standalone: false,
  templateUrl: './Shipment.component.html',
  styleUrls: ['./Shipment.component.css']
})
export class ShipmentComponent implements OnInit {

  orderId!: number;
  steps = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED'];
  currentStatus = 'PENDING';
  progressPercent = 0;

  constructor(
    private route: ActivatedRoute,
    private shipmentService: ShipmentService
  ) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('orderId'));
    this.shipmentService.getStatus(this.orderId).subscribe(status => {
      this.currentStatus = status;
      const idx = this.steps.indexOf(status);
      this.progressPercent = idx >= 0
        ? idx / (this.steps.length - 1) * 100
        : 0;
    });
  }
}
