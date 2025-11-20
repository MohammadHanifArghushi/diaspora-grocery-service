package fi.haagahelia.diaspora_grocery_service.web;

import fi.haagahelia.diaspora_grocery_service.domain.Order;
import fi.haagahelia.diaspora_grocery_service.domain.OrderRepository;
import fi.haagahelia.diaspora_grocery_service.domain.OrderStatus;
import fi.haagahelia.diaspora_grocery_service.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// this is only accessable for admin role 
//using AdminController for managing orders 
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    //display all orders
    @GetMapping("/orders")
    public String viewOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            Model model) {
        
        List<Order> orders = orderRepository.findAll();
        
        // filter the search 
        if (search != null && !search.isEmpty()) {
            orders = orders.stream()
                .filter(o -> o.getPayerEmail().toLowerCase().contains(search.toLowerCase()) ||
                            o.getRecipient().getName().toLowerCase().contains(search.toLowerCase()))
                .toList();
        }
        
        //filtring by status
        if (status != null) {
            orders = orders.stream()
                .filter(o -> o.getStatus() == status)
                .toList();
        }
        
        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("allStatuses", OrderStatus.values());
        return "admin/orders";
    }

    //details of a specific order
    @GetMapping("/orders/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            return "admin/order-details";
        }
        return "redirect:/admin/orders";
    }

    //updating oder status
    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam("status") OrderStatus status) {
        
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            orderRepository.save(order);

            // sending email notification about status update
            emailService.sendOrderStatusUpdateEmail(order);

            // we send delivery notification if status is DELIVERED
            if (status == OrderStatus.DELIVERED) {
                emailService.sendDeliveryNotificationEmail(order);
            }
        }
        
        return "redirect:/admin/orders";
    }

    // deleting one order from the database
    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/admin/orders";
    }

    // Delete all orders, clears the whole database
    @PostMapping("/orders/delete-all")
    public String deleteAllOrders() {
        orderRepository.deleteAll();
        return "redirect:/admin/orders";
    }

    // TEMPORARY TEST ENDPOINT: Send a test email to verify configuration
    // TODO: I will remove this endpoint before final version 
    // Usage: /admin/test-email?email=your@email.com
    @GetMapping("/test-email")
    @ResponseBody
    public String testEmail(@RequestParam(required = false, defaultValue = "test@example.com") String email) {
        try {
            // Create a dummy order for testing
            Order testOrder = Order.builder()
                .id(999L)
                .payerEmail(email)
                .totalAmount(new java.math.BigDecimal("50.00"))
                .status(OrderStatus.PENDING)
                .orderDate(java.time.LocalDateTime.now())
                .recipient(fi.haagahelia.diaspora_grocery_service.domain.Recipient.builder()
                    .name("Test Recipient")
                    .phone("+93 123 456 789")
                    .address("123 Test Street")
                    .city("Test City")
                    .postalCode("12345")
                    .country("Afghanistan")
                    .build())
                .build();
            
            emailService.sendOrderConfirmationEmail(testOrder);
            return "Test email sent to: " + email + " - Check your email and application logs!";
        } catch (Exception e) {
            return "Failed to send test email: " + e.getMessage();
        }
    }
}
