package fi.haagahelia.diaspora_grocery_service.web;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fi.haagahelia.diaspora_grocery_service.domain.Order;
import fi.haagahelia.diaspora_grocery_service.domain.OrderRepository;
import fi.haagahelia.diaspora_grocery_service.domain.OrderStatus;
import fi.haagahelia.diaspora_grocery_service.service.EmailService;

import java.util.Optional;

@RestController
@RequestMapping("/api/webhooks")
public class StripeWebhookController {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Value("${stripe.webhook.secret:whsec_test}")
    private String webhookSecret;

    public StripeWebhookController(OrderRepository orderRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        try {
            // we can verify the webhook signature here
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // and we can handle different event types here
            if (event.getType().equals("payment_intent.succeeded")) {
                handlePaymentIntentSucceeded(event);
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Webhook error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private void handlePaymentIntentSucceeded(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);

        if (paymentIntent != null && paymentIntent.getMetadata() != null) {
            String orderIdStr = paymentIntent.getMetadata().get("orderId");

            if (orderIdStr != null) {
                Long orderId = Long.parseLong(orderIdStr);
                Optional<Order> orderOpt = orderRepository.findById(orderId);

                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    order.setStatus(OrderStatus.PAID);
                    orderRepository.save(order);

                    // here we can send a confirmation email
                    emailService.sendPaymentConfirmationEmail(order);

                    System.out.println("Order ID: " + orderId + " marked as PAID");
                }
            }
        }
    }
}
