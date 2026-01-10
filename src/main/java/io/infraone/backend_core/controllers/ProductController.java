package io.infraone.backend_core.controllers;

import io.infraone.backend_core.models.Product;
import io.infraone.backend_core.models.User;
import io.infraone.backend_core.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER_ADMIN')")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(
            @AuthenticationPrincipal User user,
            @RequestBody Product product
    ) {
        Product saved = productService.addProduct(user.getSeller().getId(), product);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId,
            @RequestBody Product product
    ) {
        Product updated = productService.updateProduct(
                user.getSeller().getId(),
                productId,
                product
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal User user,
            @PathVariable Long productId
    ) {
        productService.deleteProduct(user.getSeller().getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                productService.getProductsBySeller(user.getSeller().getId())
        );
    }
}

