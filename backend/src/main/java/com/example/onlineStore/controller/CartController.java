package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartItemDTO;
import com.example.onlineStore.model.CartItem;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.model.ShoppingCart;
import com.example.onlineStore.repository.ShoppingCartRepository;
import com.example.onlineStore.service.CartItemService;
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
        public Long cartId;
        public Long productId;
        public int quantity;
    }

    public static class UpdateQuantityRequest {
        public int quantity;
    }

    public static class ApplyCodeRequest {
        public String code;
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
        CartItem created = service.addItem(req.cartId, req.productId, req.quantity);
        return ResponseEntity.created(URI.create("/api/cart/" + req.cartId))
                .body(toDtoEffective(created));
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemDTO> updateQuantity(@PathVariable Long cartItemId,
                                                      @RequestBody UpdateQuantityRequest req) {
        CartItem updated = service.updateQuantity(cartItemId, req.quantity);
        return ResponseEntity.ok(toDtoEffective(updated));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> updateQuantityByProduct(@PathVariable Long cartId,
                                                        @PathVariable Long productId,
                                                        @RequestBody UpdateQuantityRequest req) {
        service.updateQuantityByProduct(cartId, productId, req.quantity);
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
        CartItem updated = service.applyCode(cartId, productId, body.code);
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
