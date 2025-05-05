import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminApiService, User } from '../service/admin-api.service';

@Component({
  selector: 'app-users',
  standalone: false,
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
cancel() {
throw new Error('Method not implemented.');
}
  users: User[] = [];
  editingUser: User | null = null;
  form!: FormGroup;

  constructor(
    private api: AdminApiService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    // önce tüm müşterileri, sonra satıcıları çekip birleştiriyoruz
    this.api.getAllCustomers().subscribe(customers => {
      this.users = customers;
      this.api.getAllSellers().subscribe(sellers => {
        this.users = [...this.users, ...sellers];
      });
    });
  }

  edit(user: User) {
    this.editingUser = { ...user };
    this.form = this.fb.group({
      name:  [user.name, Validators.required],
      email: [user.email, [Validators.required, Validators.email]],
      roles: [user.roles.join(', '), Validators.required]
    });
  }

  cancelEdit() {
    this.editingUser = null;
  }

  save() {
    if (!this.form.valid || !this.editingUser) return;
    const updated: User = {
      ...this.editingUser,
      ...this.form.value,
      roles: this.form.value.roles.split(',').map((r: string) => r.trim())
    };

    // ROLE’ya göre doğru update metodunu kullan
    if (updated.roles.includes('ROLE_CUSTOMER')) {
      this.api.updateCustomer(updated.userId, updated)
        .subscribe(() => { this.loadUsers(); this.cancelEdit(); });
    } else {
      this.api.updateSeller(updated.userId, updated)
        .subscribe(() => { this.loadUsers(); this.cancelEdit(); });
    }
  }

  toggleBan(u: User) {
    if (u.roles.includes('ROLE_CUSTOMER')) {
      this.api.toggleCustomerBan(u.userId).subscribe(() => this.loadUsers());
    } else if (u.roles.includes('ROLE_SELLER')) {
      this.api.toggleSellerBan(u.userId).subscribe(() => this.loadUsers());
    }
  }
}
