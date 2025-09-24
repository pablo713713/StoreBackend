import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay, map } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private ProductUrl = 'http://localhost:8080/api/products';

  readonly list$: Observable<Product[]>;
  readonly indexById$: Observable<Map<number, Product>>;

  constructor(private http: HttpClient) {
    this.list$ = this.http.get<Product[]>(this.ProductUrl).pipe(shareReplay(1));
    this.indexById$ = this.list$.pipe(
      map(list => {
        const m = new Map<number, Product>();
        list.forEach(p => m.set(Number(p.id), p));
        return m;
      })
    );
  }

  getAll(): Observable<Product[]> { return this.http.get<Product[]>(this.ProductUrl); }
  getById(id: number): Observable<Product> { return this.http.get<Product>(`${this.ProductUrl}/${id}`); }
  createProduct(p: Product): Observable<Product> { return this.http.post<Product>(this.ProductUrl, p); }
  updateProduct(id: number, p: Product): Observable<Product> { return this.http.put<Product>(`${this.ProductUrl}/${id}`, p); }
  deleteProduct(id: number): Observable<void> { return this.http.delete<void>(`${this.ProductUrl}/${id}`); }
}
