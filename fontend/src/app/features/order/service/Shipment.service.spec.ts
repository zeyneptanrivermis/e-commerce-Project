/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ShipmentService } from './Shipment.service';

describe('Service: Shipment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ShipmentService]
    });
  });

  it('should ...', inject([ShipmentService], (service: ShipmentService) => {
    expect(service).toBeTruthy();
  }));
});
