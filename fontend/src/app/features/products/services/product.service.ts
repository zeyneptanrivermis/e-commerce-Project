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
    const backendCategory = apiProduct.mainCategory || apiProduct.category;
    const mappedCategory = mapBackendCategoryToEnum(backendCategory);

    return {
      id: apiProduct.id,
      name: apiProduct.name,
      price: apiProduct.price,
      description: apiProduct.description,
      avgRating: apiProduct.avgRating,
      shippingCost: apiProduct.shippingCost,
      stockCount: apiProduct.stockCount,
      mainCategory: mappedCategory,
      sideCategories: SideCategories[mappedCategory as keyof typeof SideCategories]?.slice(0, 2) || [],
      seller: {
        id: apiProduct.seller?.id,
        name: apiProduct.seller?.name,
        email: apiProduct.seller?.email,
      },
      reviews: apiProduct.reviews,
      cancelled: apiProduct.cancelled
    };
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getPopularProducts(count: number = 10): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/popular?count=${count}`);
  }
  
  
  
  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }
  
  //addURL(newURL:string){
  //  this.apiUrl = [...this.apiURLs, newURL];
  //}

  getProductsByCategory(category: string): Observable<Product[]> {
    return this.http.get<any[]>(`${this.apiUrl}/filter?category=${encodeURIComponent(category)}`).pipe(
      map(products => products.map(apiProduct => this.transformApiProduct(apiProduct)))
    );
  }
}

function mapBackendCategoryToEnum(backendCategory: string): MainCategory {
  if (!backendCategory) return MainCategory.Home_and_Kitchen;

  // Doğrudan enum key'iyse, eşleşeni bul
  const match = Object.keys(MainCategory).find(
    key => key.toUpperCase() === backendCategory.toUpperCase()
  );

  return match ? MainCategory[match as keyof typeof MainCategory] : MainCategory.Home_and_Kitchen;
}



