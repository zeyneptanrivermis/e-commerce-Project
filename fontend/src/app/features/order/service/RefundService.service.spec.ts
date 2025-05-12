/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RefundServiceService } from './RefundService.service';

describe('Service: RefundService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RefundServiceService]
    });
  });

  it('should ...', inject([RefundServiceService], (service: RefundServiceService) => {
    expect(service).toBeTruthy();
  }));
});
