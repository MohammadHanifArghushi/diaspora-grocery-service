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
import org.springframework.security.crypto.password.PasswordEncoder;
import fi.haagahelia.diaspora_grocery_service.domain.User;
import fi.haagahelia.diaspora_grocery_service.domain.UserRepository;

@SpringBootApplication
public class DiasporaGroceryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiasporaGroceryServiceApplication.class, args);
    }

	// below we use CommandLineRunner to seed initial data into our database
    @Bean
    public CommandLineRunner dataSeeder(
            ProductRepository productRepository, 
            CategoryRepository categoryRepository,
            UserRepository userRepository,      
            PasswordEncoder passwordEncoder) {
        
        return (args) -> {
            
            // we check if the database is empty before seeding otherwise create duplicates each time we run the application
            // when I tested this, I had 71 products because i ran it so many times 
            // and each time it was adding same products again and again
            if (categoryRepository.count() > 0) {
                System.out.println("Database already contains data. So we Skip seeding.");
                return;
            }
            
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
            
            
            Product rice = new Product(
                "Afghan Basmati Rice 10kg", 
                "Premium long grain basmati rice, essential for palaw.", 
                new BigDecimal("25.50"), 
                "/images/rice.jpg", 
                150, 
                riceCat
            );
            
            
            Product oil = new Product(
                "SunPure Cooking Oil 5L", 
                "High quality vegetable oil for daily cooking.", 
                new BigDecimal("18.99"), 
                "/images/oil.jpg", 
                80, 
                oilCat
            );
            
            
            Product flour = new Product(
                "Whole Wheat Flour 25kg", 
                "Coarse ground flour for traditional Afghan bread.", 
                new BigDecimal("15.00"), 
                "/images/flour.jpg", 
                200, 
                riceCat
            );
            
            Product greenTea = new Product(
                "Ahmad Green Cardamom Tea 500g", 
                "Popular green tea blend with hints of cardamom.", 
                new BigDecimal("8.75"), 
                "/images/greentea.jpg", 
                120, 
                teaCat
            );
            
            
            Product whiteRice = new Product(
                "Jasmine White Rice 20kg",
                "Fragrant jasmine rice, perfect for everyday meals.",
                new BigDecimal("28.99"),
                "/images/jasmine-rice.jpg",
                100,
                riceCat
            );
            
            Product pulses = new Product(
                "Mixed Lentils & Beans 5kg",
                "Nutritious mix of red lentils, chickpeas, and beans.",
                new BigDecimal("12.50"),
                "/images/lentils.jpg",
                90,
                riceCat
            );
            
            Product palmoil = new Product(
                "Red Palm Oil 2L",
                "Traditional palm oil for authentic cooking.",
                new BigDecimal("14.99"),
                "/images/palmoil.jpg",
                60,
                oilCat
            );
            
            Product ghee = new Product(
                "Clarified Butter Ghee 500ml",
                "Pure ghee for rich, traditional flavor.",
                new BigDecimal("16.75"),
                "/images/ghee.jpg",
                75,
                oilCat
            );
            
            Product blackTea = new Product(
                "Black Tea Assam 400g",
                "Strong black tea, perfect for tea lovers.",
                new BigDecimal("7.50"),
                "/images/blacktea.jpg",
                140,
                teaCat
            );
            
            Product coffee = new Product(
                "Ground Coffee Arabica 250g",
                "Premium ground coffee, smooth and aromatic.",
                new BigDecimal("9.99"),
                "/images/coffee.jpg",
                85,
                teaCat
            );
            
            productRepository.save(rice);
            productRepository.save(oil);
            productRepository.save(flour);
            productRepository.save(greenTea);
            productRepository.save(whiteRice);
            productRepository.save(pulses);
            productRepository.save(palmoil);
            productRepository.save(ghee);
            productRepository.save(blackTea);
            productRepository.save(coffee);


            System.out.println("Seeding user accounts...");
            
            
            User admin = new User(
                "admin", 
                passwordEncoder.encode("adminpass"),
                "admin@diaspora.com", 
                "ADMIN"
            );
            
            
            User regularUser = new User(
                "user", 
                passwordEncoder.encode("userpass"), 
                "user@diaspora.com", 
                "USER"
            );
            
            if (userRepository.findByUsername(admin.getUsername()).isEmpty()) {
                userRepository.save(admin);
            }

            if (userRepository.findByUsername(regularUser.getUsername()).isEmpty()) {
                userRepository.save(regularUser);
            }

            System.out.println("Data seeding complete!");
        };
    }
}