package com.example.onlineStore.service;

import com.example.onlineStore.model.CartItem;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.model.ShoppingCart;
import com.example.onlineStore.repository.CartItemRepository;
import com.example.onlineStore.repository.ProductRepository;
import com.example.onlineStore.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepo;
    private final ShoppingCartRepository cartRepo;
    private final ProductRepository productRepo;
    private final DiscountService discountService;

    public CartItemService(CartItemRepository cartItemRepo,
                           ShoppingCartRepository cartRepo,
                           ProductRepository productRepo,
                           DiscountService discountService) {
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.discountService = discountService;
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCart(Long cartId) {
        return cartItemRepo.findByCart_Id(cartId);
    }

    @Transactional
    public CartItem addItem(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");

        ShoppingCart cart = cartRepo.findById(cartId)
                .orElseGet(() -> cartRepo.save(ShoppingCart.create()));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found: " + productId));

        CartItem item = cartItemRepo.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + quantity);
                    return existing;
                })
                .orElseGet(() -> new CartItem(cart, product, quantity));

        return cartItemRepo.save(item);
    }

    @Transactional
    public CartItem updateQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new IllegalStateException("CartItem not found: " + cartItemId));
        item.setQuantity(quantity);
        return cartItemRepo.save(item);
    }

    @Transactional
    public void updateQuantityByProduct(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        CartItem item = cartItemRepo.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new IllegalStateException("CartItem not found"));
        item.setQuantity(quantity);
        cartItemRepo.save(item);
    }

    @Transactional
    public void removeItem(Long id) {
        cartItemRepo.deleteById(id);
    }

    @Transactional
    public void removeByProduct(Long cartId, Long productId) {
        CartItem item = cartItemRepo.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new IllegalStateException("CartItem not found"));
        cartItemRepo.deleteById(item.getId());
    }

    @Transactional
    public void clearCart(Long cartId) {
        cartItemRepo.deleteByCart_Id(cartId);
    }

    @Transactional
    public CartItem applyCode(Long cartId, Long productId, String code) {
        CartItem item = cartItemRepo.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new IllegalStateException("CartItem not found"));
        var od = discountService.validateForProduct(code, item.getProduct());
        if (od.isEmpty()) throw new IllegalStateException("Código inválido o no vigente");

        var d = od.get();
        item.setCouponCode(d.getIdDiscount());
        item.setCouponPct(d.getPercentage());
        return cartItemRepo.save(item);
    }

    @Transactional
    public CartItem removeCode(Long cartId, Long productId) {
        CartItem item = cartItemRepo.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new IllegalStateException("CartItem not found"));
        item.setCouponCode(null);
        item.setCouponPct(null);
        return cartItemRepo.save(item);
    }

    public BigDecimal effectiveUnitPrice(CartItem ci) {
        BigDecimal base = ci.getProduct().getPrice();
        if (ci.getCouponPct() != null) {
            return discountService.applyPct(base, ci.getCouponPct());
        }
        return base;
    }
}
