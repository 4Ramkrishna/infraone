package io.infraone.backend_core.controllers;

import io.infraone.backend_core.models.Order;
import io.infraone.backend_core.models.Seller;
import io.infraone.backend_core.models.User;
import io.infraone.backend_core.repository.SellerRepository;
import io.infraone.backend_core.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;
    private final SellerRepository sellerRepository;

    @PostMapping("/{sellerCode}")
    public ResponseEntity<Order> placeOrder(
            @AuthenticationPrincipal User user,
            @PathVariable String sellerCode,
            @RequestParam String deliveryAddress
    ) {
        Seller seller = sellerRepository.findByCode(sellerCode)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Order order = orderService.placeOrder(
                user.getId(),
                seller.getId(),
                deliveryAddress
        );
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                orderService.getOrdersForUser(user.getId())
        );
    }
}
