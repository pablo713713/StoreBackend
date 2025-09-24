import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgIf, DecimalPipe } from '@angular/common';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-payment-modal',
  standalone: true,
  imports: [FormsModule, NgIf, DecimalPipe],
  templateUrl: './payment-modal.component.html',
  styleUrls: ['./payment-modal.component.css']
})
export class PaymentModalComponent {
  @Input() total: number = 0;
  @Input() clientId!: number;
  @Output() close = new EventEmitter<void>();

  selectedMethod: 'paypal' | 'creditcard' = 'paypal';
  loading = false;

  // PayPal
  email = '';
  password = '';

  // Credit card
  cardNumber = '';
  cardHolder = '';
  expirationDate = ''; // MM/AA
  cvv = '';

  constructor(private readonly cart: CartService) {}

  // ---------- Validaciones ----------
  isValidPaypal(): boolean {
    return /\S+@\S+\.\S+/.test(this.email) && this.password.trim().length >= 4;
  }

  private luhnCheck(num: string): boolean {
    const digits = num.replace(/\D/g, '');
    let sum = 0, dbl = false;
    for (let i = digits.length - 1; i >= 0; i--) {
      let n = +digits[i];
      if (dbl) { n *= 2; if (n > 9) n -= 9; }
      sum += n; dbl = !dbl;
    }
    return digits.length >= 13 && digits.length <= 19 && sum % 10 === 0;
  }

  private expIsFuture(mmYY: string): boolean {
    const m = mmYY.match(/^(\d{2})\/(\d{2})$/);
    if (!m) return false;
    const mm = +m[1], yy = +m[2];
    if (mm < 1 || mm > 12) return false;
    const year = 2000 + yy;
    const endOfMonth = new Date(year, mm, 0);
    return new Date() <= endOfMonth;
  }

  isValidCard(): boolean {
    const raw = this.cardNumber.replace(/\s+/g, '');
    const numberOk = this.luhnCheck(raw);
    const holderOk = this.cardHolder.trim().length >= 4;
    const expOk = this.expIsFuture(this.expirationDate);
    const cvvOk = /^\d{3,4}$/.test(this.cvv);
    return numberOk && holderOk && expOk && cvvOk;
  }

  onCardNumberInput() {
    const digits = this.cardNumber.replace(/\D/g, '').slice(0, 19);
    this.cardNumber = digits.replace(/(.{4})/g, '$1 ').trim();
  }

  onExpirationInput() {
    const d = this.expirationDate.replace(/\D/g, '').slice(0, 4);
    const mm = d.slice(0, 2);
    const yy = d.slice(2);
    this.expirationDate = yy ? `${mm}/${yy}` : mm;
  }

  onCvvInput() {
    this.cvv = this.cvv.replace(/\D/g, '').slice(0, 4);
  }

  payNow() {
    if (this.total <= 0) { alert('El carrito está vacío.'); return; }
    if (this.selectedMethod === 'paypal' && !this.isValidPaypal()) return;
    if (this.selectedMethod === 'creditcard' && !this.isValidCard()) return;

    this.loading = true;
    this.cart.clear();

    setTimeout(() => {
      this.loading = false;
      alert('✅ ¡Compra exitosa! Gracias por tu pago.');
      this.close.emit();
    }, 300);
  }
}
