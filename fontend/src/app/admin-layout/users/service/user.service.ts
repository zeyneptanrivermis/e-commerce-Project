import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User }       from '../../../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  /** Admin için tüm kullanıcıları getir */
  getAll() {
    return this.http.get<User[]>('/api/admin/users');
  }

  /** Admin için tek kullanıcı getir */
  getById(id: number | string) {
    return this.http.get<User>(`/api/admin/users/${id}`);
  }

  /** Admin için kullanıcı güncelle */
  update(id: number | string, data: Partial<User>) {
    return this.http.put(`/api/admin/users/${id}`, data);
  }
}
