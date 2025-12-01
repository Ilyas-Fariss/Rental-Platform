package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.ProductRepository;
import be.ilyas.rentalplatform.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {

    private final ProductRepository productRepository;
    private final CartService cartService;

    public CartController(ProductRepository productRepository,
                          CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        model.addAttribute("items", cartService.getAllItems(session));
        return "cart";
    }

    @GetMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartService.addToCart(product, session);

        return "redirect:/catalog";
    }

    @GetMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        cartService.removeItem(productId, session);
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout")
    public String checkout(Model model, HttpSession session) {
        model.addAttribute("items", cartService.getAllItems(session));
        return "checkout";
    }
}
