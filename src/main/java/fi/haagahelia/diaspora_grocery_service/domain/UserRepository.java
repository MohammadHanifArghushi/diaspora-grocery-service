package fi.haagahelia.diaspora_grocery_service.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // we can find user by username
    Optional<User> findByUsername(String username);
}