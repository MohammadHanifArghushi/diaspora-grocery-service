package fi.haagahelia.diaspora_grocery_service.web;

import fi.haagahelia.diaspora_grocery_service.domain.*;
import fi.haagahelia.diaspora_grocery_service.service.OrderService;
import fi.haagahelia.diaspora_grocery_service.service.OrderService.OrderItemRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    // our checkout endpoint, creates order directly without payment, because i 
    //have to show a working demo without payment integration to the teacher first
    @PostMapping("/orders/checkout")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CheckoutRequest request) {
        try {
            Order order = orderService.createPendingOrder(
                request.getPayerEmail(),
                request.getRecipient(),
                request.getItems()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
            
            /* this is also a FUTURE FEATURE: Stripe Payment Integration
             * To enable payment processing, we need. to modify this to return payment intent client secret:
            
             * Map<String, String> response = new HashMap<>();
             * response.put("clientSecret", paymentIntent.getClientSecret());
             * response.put("orderId", order.getId().toString());
             * return ResponseEntity.status(HttpStatus.CREATED).body(response);
             */
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // Get current user's order history
    @GetMapping("/orders/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(@RequestParam String email) {
        try {
            List<Order> orders = orderRepository.findByPayerEmail(email);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // this endpoint is admin only
    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //here admin can update order status
    @PatchMapping("/admin/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdate statusUpdate) {
        
        Optional<Order> existingOrder = orderRepository.findById(id);
        
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setStatus(statusUpdate.getStatus());
            Order saved = orderRepository.save(order);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // we can define request body classes here or in separate files
    public static class CheckoutRequest {
        private String payerEmail;
        private Recipient recipient;
        private List<OrderItemRequest> items;

        public CheckoutRequest() {}

        public String getPayerEmail() { return payerEmail; }
        public void setPayerEmail(String payerEmail) { this.payerEmail = payerEmail; }

        public Recipient getRecipient() { return recipient; }
        public void setRecipient(Recipient recipient) { this.recipient = recipient; }

        public List<OrderItemRequest> getItems() { return items; }
        public void setItems(List<OrderItemRequest> items) { this.items = items; }
    }

    // and here we define the status update request body
    public static class StatusUpdate {
        private OrderStatus status;

        public StatusUpdate() {}

        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
    }
}
