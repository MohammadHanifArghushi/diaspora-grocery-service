package fi.haagahelia.diaspora_grocery_service.web;

import fi.haagahelia.diaspora_grocery_service.domain.Product;
import fi.haagahelia.diaspora_grocery_service.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroceryController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/")
    public String home() {
        return "home"; 
    }

    @GetMapping("/productlist")
    public String productList(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "productlist";
    }
}