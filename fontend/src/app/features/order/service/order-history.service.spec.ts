/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { OrderHistoryService } from './order-history.service';


describe('Service: OrderHistory', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrderHistoryService]
    });
  });

  it('should ...', inject([OrderHistoryService], (service: OrderHistoryService) => {
    expect(service).toBeTruthy();
  }));
});
