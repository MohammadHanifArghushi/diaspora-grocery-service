package fi.haagahelia.diaspora_grocery_service.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    public PaymentService() {
    }

    public PaymentIntent createPaymentIntent(BigDecimal amount, String email, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // we covert amount to cents because Stripe expects the amount in the smallest currency unit
        long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("eur")
                .setDescription("Order ID: " + orderId + " for Diaspora Grocery Service")
                .setReceiptEmail(email)
                .putMetadata("orderId", orderId.toString())
                .build();

        return PaymentIntent.create(params);
    }

    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        return PaymentIntent.retrieve(paymentIntentId);
    }
}
