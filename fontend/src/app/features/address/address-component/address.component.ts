import { Component, OnInit, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Address } from "../../../models/Address.model";
import { AddressService } from "../service/address.service";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css'],
  standalone: false
})
export class AddressComponent implements OnInit {
  @Input() userId!: number;

  addressForm!: FormGroup;
  addresses: Address[] = [];
  selectedAddressId: number | null = null;
  editMode: boolean = false;

  constructor(private fb: FormBuilder, private addressService: AddressService) {}

  ngOnInit(): void {
    this.addressForm = this.fb.group({
      country: ['Turkey', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      addressDetail: ['', [Validators.required, Validators.minLength(5)]]
    });

    this.loadAddresses();
  }

  loadAddresses(): void {
    this.addressService.getUserAddresses(this.userId).subscribe({
      next: data => this.addresses = data,
      error: err => console.error(err)
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
    this.resetForm();
  }

  editAddress(address: Address): void {
    this.addressForm.patchValue(address);
    this.selectedAddressId = address.addressId!;
  }

  saveAddress(): void {
    if (this.addressForm.valid) {
      const data = this.addressForm.value;

      if (this.selectedAddressId) {
        this.addressService.updateAddress(this.selectedAddressId, data).subscribe({
          next: () => {
            this.resetForm();
            this.loadAddresses();
          }
        });
      } else {
        this.addressService.addAddress(this.userId, data).subscribe({
          next: () => {
            this.resetForm();
            this.loadAddresses();
          }
        });
      }
    }
  }

  deleteAddress(addressId: number): void {
    this.addressService.deleteAddress(addressId).subscribe({
      next: () => this.loadAddresses(),
      error: err => console.error(err)
    });
  }

  resetForm(): void {
    this.addressForm.reset({ country: 'Turkey' });
    this.selectedAddressId = null;
  }
}
