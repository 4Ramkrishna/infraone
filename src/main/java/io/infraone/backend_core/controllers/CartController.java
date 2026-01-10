package io.infraone.backend_core.controllers;

import io.infraone.backend_core.models.Cart;
import io.infraone.backend_core.models.Seller;
import io.infraone.backend_core.models.User;
import io.infraone.backend_core.repository.SellerRepository;
import io.infraone.backend_core.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;
    private final SellerRepository sellerRepository;

    @PostMapping("/{sellerCode}/add")
    public ResponseEntity<Cart> addToCart(
            @AuthenticationPrincipal User user,
            @PathVariable String sellerCode,
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        Seller seller = sellerRepository.findByCode(sellerCode)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return ResponseEntity.ok(
                cartService.addToCart(
                        user.getId(),
                        seller.getId(),
                        productId,
                        quantity
                )
        );
    }

    @PutMapping("/{sellerCode}/update")
    public ResponseEntity<Cart> updateCart(
            @AuthenticationPrincipal User user,
            @PathVariable String sellerCode,
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        Seller seller = sellerRepository.findByCode(sellerCode)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return ResponseEntity.ok(
                cartService.updateCartItem(
                        user.getId(),
                        seller.getId(),
                        productId,
                        quantity
                )
        );
    }

    @GetMapping("/{sellerCode}")
    public ResponseEntity<Cart> getCart(
            @AuthenticationPrincipal User user,
            @PathVariable String sellerCode
    ) {
        Seller seller = sellerRepository.findByCode(sellerCode)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return ResponseEntity.ok(
                cartService.getCart(user.getId(), seller.getId())
        );
    }

    @DeleteMapping("/{sellerCode}/clear")
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal User authUser,
            @PathVariable String sellerCode
    ) {
        Seller seller = sellerRepository.findByCode(sellerCode)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        cartService.clearCart(authUser.getId(), seller.getId());
        return ResponseEntity.noContent().build();
    }
}
