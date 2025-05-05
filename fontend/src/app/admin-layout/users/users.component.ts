import { Component } from '@angular/core';
import { User } from '../../models/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-users',
  standalone: false,
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {
  users: User[] = [];
  editingUser: User | null = null;
  form!: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAll().subscribe(u => this.users = u);
  }

  edit(user: User) {
    this.editingUser = { ...user };
    this.form = this.fb.group({
      name:  [this.editingUser.name, Validators.required],
      email: [this.editingUser.email, [Validators.required, Validators.email]],
      roles: [this.editingUser.roles.join(', '), Validators.required]
    });
  }

  cancel() {
    this.editingUser = null;
  }

  save() {
    if (!this.form.valid || !this.editingUser) return;
    const updated: User = {
      ...this.editingUser,
      ...this.form.value,
      roles: this.form.value.roles.split(',').map((r: string) => r.trim())
    };
    this.userService.update(updated.id, updated).subscribe(() => {
      this.loadUsers();
      this.cancel();
    });
  }
}
