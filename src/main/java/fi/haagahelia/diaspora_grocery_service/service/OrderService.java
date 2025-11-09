package fi.haagahelia.diaspora_grocery_service.service;

import fi.haagahelia.diaspora_grocery_service.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RecipientRepository recipientRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, RecipientRepository recipientRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.recipientRepository = recipientRepository;
    }

    
    @Transactional
    public Order createPendingOrder(String payerEmail, Recipient recipient, List<OrderItemRequest> items) {
        final BigDecimal[] total = {BigDecimal.ZERO};
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPayerEmail(payerEmail);
        order.setRecipient(recipientRepository.save(recipient));

        
        List<OrderItem> orderItems = items.stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemReq.getProductId()));
            if (product.getStock() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            
            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total[0] = total[0].add(itemTotal);
            return OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .order(order)
                    .build();
        }).toList();

        order.setTotalAmount(total[0]);
        order.setItems(orderItems);
        return orderRepository.save(order);
    }

    
    public static class OrderItemRequest {
        private Long productId;
        private int quantity;
        public OrderItemRequest() {}
        public OrderItemRequest(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
