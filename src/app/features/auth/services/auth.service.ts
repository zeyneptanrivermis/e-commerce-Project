import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly USERS_KEY = 'users';
  private readonly CURRENT_USER_KEY = 'currentUser';

  constructor() {}

  login(email: string, password: string): boolean {
    const users = this.getUsers();
    const user = users.find(u => u.email === email && u.password === password);

    if (user) {
      localStorage.setItem(this.CURRENT_USER_KEY, JSON.stringify(user));
      return true;
    }

    return false;
  }

  register(userData: any): boolean {
    const users = this.getUsers();

    const userExists = users.some(u => u.email === userData.email);
    if (userExists) {
      return false;
    }

    users.push(userData);
    localStorage.setItem(this.USERS_KEY, JSON.stringify(users));
    localStorage.setItem(this.CURRENT_USER_KEY, JSON.stringify(userData));

    return true;
  }

  logout(): void {
    localStorage.removeItem(this.CURRENT_USER_KEY);
  }

  getCurrentUser(): any {
    const data = localStorage.getItem(this.CURRENT_USER_KEY);
    return data ? JSON.parse(data) : null;
  }

  isLoggedIn(): boolean {
    return this.getCurrentUser() !== null;
  }

  private getUsers(): any[] {
    const data = localStorage.getItem(this.USERS_KEY);
    return data ? JSON.parse(data) : [];
  }
}
