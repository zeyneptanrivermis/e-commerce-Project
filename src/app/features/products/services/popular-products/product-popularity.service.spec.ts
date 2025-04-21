import { TestBed } from '@angular/core/testing';

import { ProductPopularityService } from './product-popularity.service';

describe('ProductPopularityService', () => {
  let service: ProductPopularityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductPopularityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
