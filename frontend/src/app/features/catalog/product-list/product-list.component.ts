import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgFor, AsyncPipe, DecimalPipe } from '@angular/common';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { Product } from '../../../core/models/product.model';
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [RouterModule, NgFor, AsyncPipe, DecimalPipe, FormsModule],
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit {
  products$!: Observable<Product[]>;

  constructor(private api: ProductService, private cart: CartService) {}

  ngOnInit(){ this.products$ = this.api.getAll(); }

  add(p: Product){
    this.cart.add({
      productId: p.id,
      nameProduct: p.nameProduct,
      price: p.price,
      imageUrl: p.imageUrl,
      qty: 1
    });
  }
}
