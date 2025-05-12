/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AdminRefundService } from './AdminRefund.service';

describe('Service: AdminRefund', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AdminRefundService]
    });
  });

  it('should ...', inject([AdminRefundService], (service: AdminRefundService) => {
    expect(service).toBeTruthy();
  }));
});
