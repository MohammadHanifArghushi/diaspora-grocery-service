package fi.haagahelia.diaspora_grocery_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_user") 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    
    private String password; 

    @Column(name = "email")
    @Email(message = "Must be a valid email format")
    private String email;

    @Column(name = "role", nullable = false)
    @NotBlank(message = "Role is required (e.g., USER or ADMIN)")
    private String role;
    
    // we use below constructor for simplicity
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}