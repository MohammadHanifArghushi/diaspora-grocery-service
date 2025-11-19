package fi.haagahelia.diaspora_grocery_service.web;

import fi.haagahelia.diaspora_grocery_service.domain.CategoryRepository;
import fi.haagahelia.diaspora_grocery_service.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GroceryController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/")
    public String home() {
        return "home"; 
    }

    @GetMapping("/shop")
    public String shop(Model model, @RequestParam(required = false) Long categoryId) {
        model.addAttribute("categories", categoryRepository.findAll());
        
        if (categoryId != null) {
            model.addAttribute("products", productRepository.findByCategoryId(categoryId));
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        
        return "shop";
    }
    
    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("stripePublicKey", stripePublicKey);
        return "checkout";
    }
    
    @GetMapping("/order-success")
    public String orderSuccess() {
        return "order-success";
    }

    
    @GetMapping("/productlist")
    public String productList(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "productlist";
    }
}