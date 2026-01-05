package io.infraone.backend_core.service;

import io.infraone.backend_core.models.*;
import io.infraone.backend_core.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Long userId, Long sellerId, String deliveryAddress) {

        Cart cart = cartRepository.findByUserIdAndSellerId(userId, sellerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setSeller(cart.getSeller());
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryAddress(deliveryAddress);

        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {

            Product product = productRepository
                    .findByIdAndSellerId(cartItem.getProduct().getId(), sellerId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            // Deduct inventory
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // snapshot

            orderItemRepository.save(orderItem);
        }

        // Clear cart
        cartItemRepository.deleteByCartId(cart.getId());

        return order;
    }

    public List<Order> getOrdersForUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersForSeller(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Transactional
    public Order updateOrderStatus(Long sellerId, Long orderId, OrderStatus status) {
        Order order = orderRepository.findByIdAndSellerId(orderId, sellerId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}