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
  public apiUrl: string="http://localhost:8080/api/products";

  constructor() {}

  getProducts(limit: number, skip: number): Observable<Product[]> {
    return this.http.get<any[]>(`${this.apiUrl}/paged?limit=${limit}&skip=${skip}`).pipe(
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
      seller: apiProduct.store,
      mainCategory: mappedCategory,
      sideCategories: SideCategories[mappedCategory].slice(0, 2), // Optional logic
    };
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getPopularProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/popular`);
  }

  //addURL(newURL:string){
  //  this.apiUrl = [...this.apiURLs, newURL];
  //}


}
