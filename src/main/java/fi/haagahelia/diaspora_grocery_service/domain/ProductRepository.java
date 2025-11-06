package fi.haagahelia.diaspora_grocery_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
