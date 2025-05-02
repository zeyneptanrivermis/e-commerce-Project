import { Component, OnInit, Input } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Address, City, District, CITY_DISTRICT_MAP } from "../../../models/Address.model";
import { AddressService } from "../service/address.service";

@Component({
  selector: 'app-address',
  standalone:false,
  templateUrl: './address.component.html',
  styleUrl: './address.component.css'
})
export class AddressComponent implements OnInit {
  @Input() userId!: number;

  addressForm!: FormGroup;
  addresses: Address[] = [];
  cities = Object.values(City);
  districts = Object.values(District);
  filteredDistricts: District[] = [];

  constructor(private fb: FormBuilder, private addressService: AddressService) {}

  ngOnInit(): void {
    this.addressForm = this.fb.group({
      country: ['Turkey'],
      city: ['', Validators.required],
      district: ['', Validators.required]
    });
      this.addressForm.get('city')?.valueChanges.subscribe(() => this.onCityChange());
    this.loadAddresses();
  }

  onCityChange(): void {
    const selectedCity = this.addressForm.get('city')?.value as keyof typeof CITY_DISTRICT_MAP;
    this.filteredDistricts = CITY_DISTRICT_MAP[selectedCity] || [];
    this.addressForm.get('district')?.setValue(''); // seçimi sıfırla
  }

  loadAddresses(): void {
    this.addressService.getUserAddresses(this.userId).subscribe({
      next: data => this.addresses = data,
      error: err => console.error(err)
    });
  }

  onSubmit(): void {
    if (this.addressForm.valid) {
      this.addressService.addAddress(this.userId, this.addressForm.value).subscribe({
        next: () => {
          this.addressForm.reset({ country: 'Turkey' });
          this.loadAddresses();
        },
        error: err => console.error(err)
      });
    }
  }
}
