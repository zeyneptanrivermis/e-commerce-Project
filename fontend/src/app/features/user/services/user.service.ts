import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../../models/user.model';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/api/user'; // Backend API'si

  constructor(private http: HttpClient) { }

  // Kullanıcı bilgilerini alma
  getUserInfo(userId: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/info/${userId}`);
  }
}

