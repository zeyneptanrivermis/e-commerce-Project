import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product, MainCategory, SideCategories } from '../../../models/product.model';
import { inject } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private http = inject(HttpClient);
  public apiUrl: string="https://fakestoreapi.com/products";

  constructor() {}

  getProducts(limit: number, skip: number): Observable<Product[]> {
    return this.http.get<any[]>(`${this.apiUrl}?limit=${limit}&skip=${skip}`).pipe(
      map(products => products.map(apiProduct => this.transformApiProduct(apiProduct)))
    );
  }
  
  private transformApiProduct(apiProduct: any): Product {
    // Normalize category from API to match your enum
    const categoryMap: Record<string, MainCategory> = {
      "men's clothing": MainCategory.Clothing,
      "women's clothing": MainCategory.Clothing,
      "jewelry": MainCategory.Hobbies,
      "hobby products": MainCategory.Hobbies,
      "electronics": MainCategory.Electronics,
    };
  
    const mappedCategory = categoryMap[apiProduct.category] || MainCategory.Home_and_Kitchen;
  
    return {
      id: apiProduct.id,
      name: apiProduct.title,
      description: apiProduct.description,
      price: apiProduct.price,
      mainCategory: mappedCategory,
      sideCategories: SideCategories[mappedCategory].slice(0, 2), // Optional logic
      isFavourite: false
    };
  }
  
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  //addURL(newURL:string){
  //  this.apiUrl = [...this.apiURLs, newURL];
  //}


}
