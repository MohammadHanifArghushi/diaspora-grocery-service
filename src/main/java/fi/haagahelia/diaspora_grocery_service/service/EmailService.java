package fi.haagahelia.diaspora_grocery_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import fi.haagahelia.diaspora_grocery_service.domain.Order;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmationEmail(Order order) {
        String subject = "Order Confirmation, Diaspora Grocery Service";
        String body = buildOrderConfirmationEmail(order);

        sendEmail(order.getPayerEmail(), subject, body);
    }

    public void sendOrderStatusUpdateEmail(Order order) {
        String subject = "Order Status Update, Order ID:" + order.getId();
        String body = buildOrderStatusUpdateEmail(order);

        sendEmail(order.getPayerEmail(), subject, body);

        // We can also send a notification to the recipient if their contact is available
        if (order.getRecipient() != null) {
            String recipientSubject = "Your Diaspora Grocery Order Status Update, Order ID:" + order.getId();
            String recipientBody = buildRecipientStatusUpdateEmail(order);
            sendEmail(order.getRecipient().getPhone(), recipientSubject, recipientBody);
        }
    }

    public void sendPaymentConfirmationEmail(Order order) {
        String subject = "Payment Received for Order ID:" + order.getId();
        String body = buildPaymentConfirmationEmail(order);

        sendEmail(order.getPayerEmail(), subject, body);
    }

    public void sendDeliveryNotificationEmail(Order order) {
        String subject = "Your Order Has Been Delivered, Order ID:" + order.getId();
        String body = buildDeliveryNotificationEmail(order);

        sendEmail(order.getPayerEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildOrderConfirmationEmail(Order order) {
        return "Hello " + order.getPayerEmail() + ",\n\n" +
                "Your order ID:" + order.getId() + " has been received!\n\n" +
                "Order Details:\n" +
                "Recipient: " + order.getRecipient().getName() + "\n" +
                "Address: " + order.getRecipient().getAddress() + ", " + order.getRecipient().getCity() + "\n" +
                "Total Amount: €" + order.getTotalAmount() + "\n" +
                "Status: " + order.getStatus() + "\n\n" +
                "We will process your payment shortly.\n\n" +
                "Thank you for using Diaspora Grocery Service!\n" +
                "Best regards,\nDiaspora Grocery Team";
    }

    private String buildOrderStatusUpdateEmail(Order order) {
        return "Hello " + order.getPayerEmail() + ",\n\n" +
                "Your order #" + order.getId() + " status has been updated.\n\n" +
                "New Status: " + order.getStatus() + "\n" +
                "Recipient: " + order.getRecipient().getName() + "\n" +
                "Amount: €" + order.getTotalAmount() + "\n\n" +
                "We appreciate your business!\n" +
                "Best regards,\nDiaspora Grocery Team";
    }

    private String buildRecipientStatusUpdateEmail(Order order) {
        return "Hello " + order.getRecipient().getName() + ",\n\n" +
                "Your groceries order status has been updated.\n\n" +
                "Status: " + order.getStatus() + "\n" +
                "Contact: " + order.getRecipient().getPhone() + "\n\n" +
                "Someone has sent you groceries through Diaspora Grocery Service!\n" +
                "Best regards,\nDiaspora Grocery Team";
    }

    private String buildPaymentConfirmationEmail(Order order) {
        return "Hello " + order.getPayerEmail() + ",\n\n" +
                "Payment received for order #" + order.getId() + "!\n\n" +
                "Amount Paid: €" + order.getTotalAmount() + "\n" +
                "Recipient: " + order.getRecipient().getName() + "\n" +
                "Status: PAID\n\n" +
                "Your order is now being processed for delivery.\n" +
                "Best regards,\nDiaspora Grocery Team";
    }

    private String buildDeliveryNotificationEmail(Order order) {
        return "Hello " + order.getPayerEmail() + ",\n\n" +
                "Your order ID:" + order.getId() + " has been delivered!\n\n" +
                "Recipient: " + order.getRecipient().getName() + "\n" +
                "Location: " + order.getRecipient().getCity() + "\n\n" +
                "Thank you for using Diaspora Grocery Service. We hope your loved ones enjoyed their groceries!\n" +
                "Best regards,\nDiaspora Grocery Team";
    }
}
