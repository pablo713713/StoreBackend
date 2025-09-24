export interface Product {
  id: number;
  idProduct: string;
  nameProduct: string;

  price: number;
  stock: number;

  imageUrl: string;
  description?: string;
  color?: string;
}

export interface CartItem {
  productId: number;
    nameProduct: string;
    price: number;
    qty: number;
    imageUrl: string;
}


export interface AddItemRequest {
  cartId: number;
  productId: number;
  quantity: number;
  imageUrl?: string;
}
