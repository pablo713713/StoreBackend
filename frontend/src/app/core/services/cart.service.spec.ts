import { TestBed } from '@angular/core/testing';
import { CartService } from './cart.service';
import { CartItem } from '../models/product.model';
import { firstValueFrom } from 'rxjs';
import { take } from 'rxjs/operators';

describe('CartService', () => {
  let service: CartService;
  const mockItem: CartItem = {
    productId: '1',
    title: 'Test Product',
    price: 100,
    imageUrl: 'test.png',
    qty: 1
  };

  beforeEach(() => {
    localStorage.clear(); 
    TestBed.configureTestingModule({});
    service = TestBed.inject(CartService);
  });

  it('should start empty', (done) => {
    service.items$.subscribe(items => {
      expect(items.length).toBe(0);
      done();
    });
  });

  it('should add a new item', (done) => {
    service.add(mockItem);
    service.items$.subscribe(items => {
      expect(items.length).toBe(1);
      expect(items[0].title).toBe('Test Product');
      done();
    });
  });

  it('should increase qty if item already exists', (done) => {
    service.add(mockItem);
    service.add(mockItem);
    service.items$.subscribe(items => {
      expect(items[0].qty).toBe(2);
      done();
    });
  });

  it('should update qty', (done) => {
    service.add(mockItem);
    service.update(mockItem.productId, 5);
    service.items$.subscribe(items => {
      expect(items[0].qty).toBe(5);
      done();
    });
  });

  it('should enforce minimum qty of 1', (done) => {
    service.add(mockItem);
    service.update(mockItem.productId, 0);
    service.items$.subscribe(items => {
      expect(items[0].qty).toBe(1);
      done();
    });
  });

  it('should remove item', (done) => {
    service.add(mockItem);
    service.remove(mockItem.productId);
    service.items$.subscribe(items => {
      expect(items.length).toBe(0);
      done();
    });
  });

  it('should clear cart', (done) => {
    service.add(mockItem);
    service.clear();
    service.items$.subscribe(items => {
      expect(items.length).toBe(0);
      done();
    });
  });

  it('should calculate total$', (done) => {
    service.add({ ...mockItem, qty: 2 });
    service.total$.subscribe(total => {
      expect(total).toBe(200);
      done();
    });
  });

  it('should persist items to localStorage', () => {
  service.add(mockItem);
  const saved = JSON.parse(localStorage.getItem('biba.cart.v1') || '[]');
  expect(saved.length).toBe(1);
  expect(saved[0].productId).toBe(mockItem.productId);
  });

  it('should handle product with zero price in total$', async () => {
    service.add({ ...mockItem, productId: '1', price: 0, qty: 3 });
    const total = await firstValueFrom(service.total$.pipe(take(1)));
    expect(total).toBe(0);
  });
});
