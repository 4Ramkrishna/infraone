package io.infraone.backend_core.repository;

import io.infraone.backend_core.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndSellerId(Long userId, Long sellerId);

    boolean existsByUserIdAndSellerId(Long userId, Long sellerId);

}
