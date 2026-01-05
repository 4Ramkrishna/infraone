package io.infraone.backend_core.service;

import io.infraone.backend_core.models.Product;
import io.infraone.backend_core.models.Seller;
import io.infraone.backend_core.repository.ProductRepository;
import io.infraone.backend_core.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public Product addProduct(Long sellerId, Product product) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        product.setSeller(seller);
        return productRepository.save(product);
    }

    public Product updateProduct(Long sellerId, Long productId, Product updated) {
        Product product = productRepository.findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updated.getName());
        product.setPrice(updated.getPrice());
        product.setQuantity(updated.getQuantity());

        return productRepository.save(product);
    }

    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}