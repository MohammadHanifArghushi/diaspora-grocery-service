package fi.haagahelia.diaspora_grocery_service.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name cannot be empty")
    private String name;

    // One Category can have many Products in our database
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    
    @JsonIgnoreProperties("category") 
    private List<Product> products; 
    
    // constructor for easy seeding
    public Category(String name) {
        this.name = name;
    }
}
