import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AddItemRequest, CartItem } from '../models/product.model';

@Injectable({ providedIn: 'root' })
export class CartApiService {
  private base = `${environment.apiUrl}/cart`;

  constructor(private http: HttpClient) {}

  getCart(cartId: number): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(`${this.base}/${cartId}`);
  }

  addItem(body: AddItemRequest): Observable<CartItem> {
    return this.http.post<CartItem>(`${this.base}/items`, body);
  }

  updateQtyByProduct(cartId: number, productId: number, qty: number): Observable<void> {
    return this.http.put<void>(`${this.base}/${cartId}/items/${productId}`, { quantity: qty });
  }

  removeByProduct(cartId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${cartId}/items/${productId}`);
  }

  clear(cartId: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${cartId}/clear`);
  }

  applyCode(cartId: number, productId: number, code: string): Observable<CartItem> {
    return this.http.post<CartItem>(`${this.base}/${cartId}/items/${productId}/apply-code`, { code });
  }

  removeCode(cartId: number, productId: number): Observable<CartItem> {
    return this.http.delete<CartItem>(`${this.base}/${cartId}/items/${productId}/apply-code`);
  }
}
