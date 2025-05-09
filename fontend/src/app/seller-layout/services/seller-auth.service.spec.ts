import { TestBed } from '@angular/core/testing';

import { SellerAuthService } from './seller-auth.service';

describe('SellerAuthService', () => {
  let service: SellerAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SellerAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
