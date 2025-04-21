import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product } from '../../../models/product.model';
import { inject } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private http = inject(HttpClient);
  public apiUrl: string="https://fakestoreapi.com/products";

  constructor() {}

  getProducts(limit: number, skip: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}?limit=${limit}&skip=${skip}`);
  }
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  //addURL(newURL:string){
  //  this.apiUrl = [...this.apiURLs, newURL];
  //}


}
