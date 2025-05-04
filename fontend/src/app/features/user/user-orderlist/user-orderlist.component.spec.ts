import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserOrderlistComponent } from './user-orderlist.component';

describe('UserOrderlistComponent', () => {
  let component: UserOrderlistComponent;
  let fixture: ComponentFixture<UserOrderlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserOrderlistComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserOrderlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
