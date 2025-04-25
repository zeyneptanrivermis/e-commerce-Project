import { ManageProductService } from './manage-products.service';
import { TestBed } from '@angular/core/testing';

describe('ManageProductsService', () => {
  let service: ManageProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManageProductService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
