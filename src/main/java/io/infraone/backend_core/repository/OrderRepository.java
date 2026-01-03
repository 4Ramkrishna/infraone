package io.infraone.backend_core.repository;

import io.infraone.backend_core.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findBySellerId(Long sellerId);

    Optional<Order> findByIdAndSellerId(Long orderId, Long sellerId);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

}
