package fi.haagahelia.diaspora_grocery_service.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fi.haagahelia.diaspora_grocery_service.domain.Category;
import fi.haagahelia.diaspora_grocery_service.domain.CategoryRepository;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class CategoryRestController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    // Get single category by ID
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
