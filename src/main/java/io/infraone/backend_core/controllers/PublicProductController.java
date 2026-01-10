package io.infraone.backend_core.controllers;

import io.infraone.backend_core.models.Product;
import io.infraone.backend_core.models.Seller;
import io.infraone.backend_core.repository.SellerRepository;
import io.infraone.backend_core.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/sellers/{sellerCode}/products")
@RequiredArgsConstructor
public class PublicProductController {

    private final SellerRepository sellerRepository;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getSellerProducts(
            @PathVariable String sellerCode
    ) {
        Seller seller = sellerRepository.findByCode(sellerCode)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        return ResponseEntity.ok(
                productService.getProductsBySeller(seller.getId())
        );
    }
}