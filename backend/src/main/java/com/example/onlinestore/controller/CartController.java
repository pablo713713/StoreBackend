package com.example.onlinestore.controller;

import com.example.onlinestore.dto.CartItemDTO;
import com.example.onlinestore.model.CartItem;
import com.example.onlinestore.model.Product;
import com.example.onlinestore.model.ShoppingCart;
import com.example.onlinestore.repository.ShoppingCartRepository;
import com.example.onlinestore.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemService service;
    private final ShoppingCartRepository cartRepo;

    public CartController(CartItemService service, ShoppingCartRepository cartRepo) {
        this.service = service;
        this.cartRepo = cartRepo;
    }

    public static class AddItemRequest {
        private Long cartId;
        private Long productId;
        private int quantity;

        public Long getCartId() {
            return cartId;
        }

        public void setCartId(Long cartId) {
            this.cartId = cartId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class UpdateQuantityRequest {
        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class ApplyCodeRequest {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    @GetMapping("/{cartId}")
    public List<CartItemDTO> getCart(@PathVariable Long cartId) {
        return service.getCart(cartId).stream()
                .map(this::toDtoEffective)
                .toList();
    }

    @PostMapping("/ensure")
    public ResponseEntity<Long> ensureCart(@RequestParam(required = false) Long cartId) {
        ShoppingCart sc = (cartId != null && cartRepo.findById(cartId).isPresent())
                ? cartRepo.findById(cartId).get()
                : cartRepo.save(ShoppingCart.create());
        return ResponseEntity.ok(sc.getId());
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemDTO> addItem(@RequestBody AddItemRequest req) {
        CartItem created = service.addItem(req.getCartId(), req.getProductId(), req.getQuantity());
        return ResponseEntity.created(URI.create("/api/cart/" + req.getCartId()))
                .body(toDtoEffective(created));
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemDTO> updateQuantity(@PathVariable Long cartItemId,
                                                      @RequestBody UpdateQuantityRequest req) {
        CartItem updated = service.updateQuantity(cartItemId, req.getQuantity());
        return ResponseEntity.ok(toDtoEffective(updated));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> updateQuantityByProduct(@PathVariable Long cartId,
                                                        @PathVariable Long productId,
                                                        @RequestBody UpdateQuantityRequest req) {
        service.updateQuantityByProduct(cartId, productId, req.getQuantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeByProduct(@PathVariable Long cartId, @PathVariable Long productId) {
        service.removeByProduct(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartItemId) {
        service.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        service.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/items/{productId}/apply-code")
    public ResponseEntity<CartItemDTO> applyCode(@PathVariable Long cartId,
                                                 @PathVariable Long productId,
                                                 @RequestBody ApplyCodeRequest body) {
        CartItem updated = service.applyCode(cartId, productId, body.getCode());
        return ResponseEntity.ok(toDtoEffective(updated));
    }

    @DeleteMapping("/{cartId}/items/{productId}/apply-code")
    public ResponseEntity<CartItemDTO> removeCode(@PathVariable Long cartId,
                                                  @PathVariable Long productId) {
        CartItem updated = service.removeCode(cartId, productId);
        return ResponseEntity.ok(toDtoEffective(updated));
    }

    private CartItemDTO toDtoEffective(CartItem entity) {
        Product p = entity.getProduct();
        var unit = service.effectiveUnitPrice(entity);
        return new CartItemDTO(
                p.getId(),
                p.getNameProduct(),
                unit,
                entity.getQuantity()
        );
    }
}
