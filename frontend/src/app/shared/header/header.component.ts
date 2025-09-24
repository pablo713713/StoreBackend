import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, AsyncPipe, FormsModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  count$!: Observable<number>;

  constructor(
    private cart: CartService,
    private router: Router   
  ) {}

  ngOnInit(): void {
    this.count$ = this.cart.count$;
  }

  open() {
    document.querySelector('app-cart-drawer')
      ?.dispatchEvent(new CustomEvent('open'));
  }

  onLogin() {
    this.router.navigate(['/login']); 
  }
}
