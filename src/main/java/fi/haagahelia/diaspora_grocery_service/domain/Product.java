package fi.haagahelia.diaspora_grocery_service.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must not be negative")
    private BigDecimal price; // We have used big decimal for precise monetary values

    private String imageUrl;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock must be not be a negative number")
    private Integer stock;

    // Relationship: Many Products belong to One Category
    @ManyToOne
    @JoinColumn(name = "categoryid") // This is our foreign key form category class
   
    @JsonIgnoreProperties("products") 
    private Category category;

    // simple constructor for easy seeding
    public Product(String name, String description, BigDecimal price, String imageUrl, Integer stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.category = category;
    }
}
