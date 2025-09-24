import { Routes } from '@angular/router';
import { ProductListComponent } from './features/catalog/product-list/product-list.component';
import { ProductDetailComponent } from './features/catalog/product-detail/product-detail.component';
import { LoginComponent } from './features/auth/login/login.component';

export const routes: Routes = [
  { path: '', component: ProductListComponent },
  { path: 'product/:id', component: ProductDetailComponent },
  { path: 'login', component: LoginComponent },   
  { path: '**', redirectTo: '' }                  
];

