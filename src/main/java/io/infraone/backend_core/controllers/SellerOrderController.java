package io.infraone.backend_core.controllers;

import io.infraone.backend_core.models.Order;
import io.infraone.backend_core.models.OrderStatus;
import io.infraone.backend_core.models.User;
import io.infraone.backend_core.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                orderService.getOrdersForSeller(user.getSeller().getId())
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        user.getSeller().getId(),
                        orderId,
                        status
                )
        );
    }
}

