package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.Category;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.CategoryRepository;
import be.ilyas.rentalplatform.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CatalogController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CatalogController(ProductRepository productRepository,
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }



    @GetMapping("/catalog")
    public String showCatalog(@RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String search,
                              Model model) {

        List<Category> categories = categoryRepository.findAll();
        List<Product> products;

        // ✅ Altijd teruggeven aan de view
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("search", search);

        if (categoryId != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                products = productRepository.findByCategory(categoryOpt.get());
            } else {
                products = productRepository.findAll();
            }
        } else if (search != null && !search.isBlank()) {
            products = productRepository.findByNameContainingIgnoreCase(search);
        } else {
            products = productRepository.findAll();
        }

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        return "catalog";
    }}
