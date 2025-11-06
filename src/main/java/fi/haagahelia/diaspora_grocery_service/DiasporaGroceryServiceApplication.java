package fi.haagahelia.diaspora_grocery_service;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import fi.haagahelia.diaspora_grocery_service.domain.Category;
import fi.haagahelia.diaspora_grocery_service.domain.CategoryRepository;
import fi.haagahelia.diaspora_grocery_service.domain.Product;
import fi.haagahelia.diaspora_grocery_service.domain.ProductRepository;

@SpringBootApplication
public class DiasporaGroceryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiasporaGroceryServiceApplication.class, args);
    }

	// below we use CommandLineRunner to seed initial data into our database
    @Bean
    public CommandLineRunner dataSeeder(
            ProductRepository productRepository, 
            CategoryRepository categoryRepository) {
        
        return (args) -> {
            
            // first we create and save some categories as below
            System.out.println("Seeding categories...");
            Category riceCat = new Category("Staple Grains");
            Category oilCat = new Category("Cooking Oil & Fats");
            Category teaCat = new Category("Tea & Beverages");
            
            categoryRepository.save(riceCat);
            categoryRepository.save(oilCat);
            categoryRepository.save(teaCat);
            
            // second we create and save some products for the categories created above
            System.out.println("Seeding products...");
            
            // Product1: Linked to Staple Grains category which is riceCat
            Product rice = new Product(
                "Afghan Basmati Rice 10kg", 
                "Premium long grain basmati rice, essential for palaw.", 
                new BigDecimal("25.50"), 
                "/images/rice.jpg", 
                150, 
                riceCat
            );
            
            // Product 2:Linked to Cooking Oil & Fats category which is oilCat
            Product oil = new Product(
                "SunPure Cooking Oil 5L", 
                "High quality vegetable oil for daily cooking.", 
                new BigDecimal("18.99"), 
                "/images/oil.jpg", 
                80, 
                oilCat
            );
            
            // Product 3:Linked to Staple Grains category which is riceCat
            Product flour = new Product(
                "Whole Wheat Flour 25kg", 
                "Coarse ground flour for traditional Afghan bread.", 
                new BigDecimal("15.00"), 
                "/images/flour.jpg", 
                200, 
                riceCat
            );
            
            // Product 4: Linked to Tea & Beverages category which is teaCat
            Product greenTea = new Product(
                "Ahmad Green Cardamom Tea 500g", 
                "Popular green tea blend with hints of cardamom.", 
                new BigDecimal("8.75"), 
                "/images/greentea.jpg", 
                120, 
                teaCat
            );
            
            productRepository.save(rice);
            productRepository.save(oil);
            productRepository.save(flour);
            productRepository.save(greenTea);

            System.out.println("Data seeding complete!");
        };
    }
}