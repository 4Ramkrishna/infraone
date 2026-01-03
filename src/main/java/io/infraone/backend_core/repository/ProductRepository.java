package io.infraone.backend_core.repository;

import io.infraone.backend_core.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySellerId(Long sellerId);

    Optional<Product> findByIdAndSellerId(Long productId, Long sellerId);

    boolean existsByIdAndSellerId(Long productId, Long sellerId);

}