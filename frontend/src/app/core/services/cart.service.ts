import { Injectable } from '@angular/core';
import { BehaviorSubject, map, combineLatest, Observable } from 'rxjs';
import { CartItem } from '../models/product.model';
import { CartApiService } from './cart-api.service';
import { ProductService } from './product.service';

const CART_ID = 1;

@Injectable({ providedIn: 'root' })
export class CartService {
  private _rawItems$ = new BehaviorSubject<CartItem[]>([]);

  readonly items$: Observable<CartItem[]>;
  readonly count$: Observable<number>;
  readonly total$: Observable<number>;

  constructor(private api: CartApiService, private products: ProductService) {

    this.items$ = combineLatest([this._rawItems$, this.products.indexById$]).pipe(
      map(([items, idx]) =>
        items.map(i => {
          const p = idx.get(Number(i.productId));
          const imageUrl =
            (p?.imageUrl) ??
            (p?.id ? `assets/img/${p.id}.png` : 'assets/img/placeholder.png');
          return { ...i, imageUrl };
        })
      )
    );

    this.count$ = this.items$.pipe(map(xs => xs.reduce((s, i) => s + i.qty, 0)));
    this.total$ = this.items$.pipe(map(xs => xs.reduce((s, i) => s + i.qty * Number(i.price), 0)));

    this.refresh();
  }

  private refresh() {
    this.api.getCart(CART_ID).subscribe(items => this._rawItems$.next(items ?? []));
  }

  add(item: CartItem) {
    this.api.addItem({ cartId: CART_ID, productId: item.productId, quantity: item.qty })
      .subscribe({ next: () => this.refresh() });
  }

  addThenApply(item: CartItem, code?: string) {
    this.api.addItem({ cartId: CART_ID, productId: item.productId, quantity: item.qty })
      .subscribe({
        next: () => {
          const c = code?.trim();
          if (c) {
            this.applyCode(item.productId, c);
          } else {
            this.refresh();
          }
        }
      });
  }

  update(productId: number, qty: number) {
    this.api.updateQtyByProduct(CART_ID, productId, Math.max(1, qty))
      .subscribe({ next: () => this.refresh() });
  }

  remove(productId: number) {
    this.api.removeByProduct(CART_ID, productId)
      .subscribe({ next: () => this.refresh() });
  }

  clear() {
    this.api.clear(CART_ID).subscribe({ next: () => this.refresh() });
  }

  applyCode(productId: number, code: string) {
    const c = code?.trim();
    if (!c) return;
    this.api.applyCode(CART_ID, productId, c)
      .subscribe({ next: () => this.refresh() });
  }

  removeCode(productId: number) {
    this.api.removeCode(CART_ID, productId)
      .subscribe({ next: () => this.refresh() });
  }
}
