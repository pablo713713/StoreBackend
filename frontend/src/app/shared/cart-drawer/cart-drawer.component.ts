import { Component, HostListener, OnInit } from '@angular/core';
import { NgFor, NgIf, AsyncPipe, DecimalPipe } from '@angular/common';
import { CartService } from '../../core/services/cart.service';
import { Observable } from 'rxjs';
import { CartItem } from '../../core/models/product.model';
import { FormsModule } from '@angular/forms';
import { PaymentModalComponent } from '../payment-modal/payment-modal.component';

@Component({
  selector: 'app-cart-drawer',
  standalone: true,
  imports: [
    NgFor,
    NgIf,
    AsyncPipe,
    DecimalPipe,
    FormsModule,
    PaymentModalComponent
  ],
  templateUrl: './cart-drawer.component.html',
  styleUrls: ['./cart-drawer.component.css']
})
export class CartDrawerComponent implements OnInit {
  openDrawer = false;

  items$!: Observable<CartItem[]>;
  total$!: Observable<number>;

  showPayment: boolean = false;

  codes: Record<number, string> = {};

  constructor(private readonly cart: CartService) {}

  ngOnInit(): void {
    this.items$ = this.cart.items$;
    this.total$ = this.cart.total$;
  }

  openPayment() { this.showPayment = true; }
  closePayment() { this.showPayment = false; }

  @HostListener('open')
  onOpen() { this.openDrawer = true; }

  close() { this.openDrawer = false; }

  remove(id: any) { this.cart.remove(id); }

  update(id: any, qty: number) { this.cart.update(id, qty); }

  apply(productId: number) {
    const code = (this.codes[productId] || '').trim();
    if (code) this.cart.applyCode(productId, code);
  }

  removeCode(productId: number) {
    this.cart.removeCode(productId);
    this.codes[productId] = '';
  }
}
