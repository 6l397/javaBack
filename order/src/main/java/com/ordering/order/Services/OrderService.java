package com.ordering.order.Services;

import com.ordering.order.Entities.Order;
import com.ordering.order.Client.ReviewClient;
import com.ordering.order.Repositories.OrderRepository;
import com.ordering.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReviewClient reviewClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public Order createOrder(List<Long> reviewIds) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .status("CREATED")
                .totalPrice(totalPrice)
                .build();

        Order savedOrder = orderRepository.save(order);

        var orderPlacedEvent = new OrderPlacedEvent(savedOrder.getStatus());
        kafkaTemplate.send("order-placed", orderPlacedEvent);

        return savedOrder;
    }

    private Integer getReviewRating(Long reviewId) {
        return reviewClient.getReviewRating(reviewId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        orderRepository.save(order);
        log.info("Order {} status updated to {}", orderId, newStatus);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }

        orderRepository.deleteById(orderId);
        log.info("Order {} has been deleted", orderId);
    }
}
