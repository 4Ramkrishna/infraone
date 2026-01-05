package io.infraone.backend_core.service;

import io.infraone.backend_core.models.*;
import io.infraone.backend_core.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart addToCart(Long userId, Long sellerId, Long productId, Integer quantity) {

        Product product = productRepository.findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        Cart cart = cartRepository.findByUserIdAndSellerId(userId, sellerId)
                .orElseGet(() -> createCart(userId, sellerId));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        cartItemRepository.save(item);
        return cart;
    }

    @Transactional
    public Cart updateCartItem(Long userId, Long sellerId, Long productId, Integer quantity) {
        Cart cart = getCart(userId, sellerId);

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return cart;
    }

    @Transactional
    public void clearCart(Long userId, Long sellerId) {
        Cart cart = getCart(userId, sellerId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    public Cart getCart(Long userId, Long sellerId) {
        return cartRepository.findByUserIdAndSellerId(userId, sellerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    private Cart createCart(Long userId, Long sellerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setSeller(seller);
        return cartRepository.save(cart);
    }
}