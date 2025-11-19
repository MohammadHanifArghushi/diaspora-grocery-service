package fi.haagahelia.diaspora_grocery_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    // finding products by category
    List<Product> findByCategory(Category category);
    
    // finding products by category ID
    List<Product> findByCategoryId(Long categoryId);
}
