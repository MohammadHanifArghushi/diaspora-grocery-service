package fi.haagahelia.diaspora_grocery_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders by payer email (this is for user order history)
    List<Order> findByPayerEmail(String payerEmail);
}
