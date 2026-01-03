package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.ProductRepository;
import be.ilyas.rentalplatform.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepo;
    private final CartService cartService;

    public CartController(ProductRepository productRepo, CartService cartService) {
        this.productRepo = productRepo;
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        model.addAttribute("items", cartService.getAllItems(session));
        model.addAttribute("total", cartService.calculateTotal(session));
        model.addAttribute("today", LocalDate.now()); // ✅ nodig voor date picker
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        Product product = productRepo.findById(id).orElse(null);
        if (product != null) {
            cartService.addToCart(product, session);
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeFromCart(id, session);
        return "redirect:/cart";
    }

    // ✅ NIEUW: einddatum per item aanpassen
    @PostMapping("/update-date/{id}")
    public String updateEndDate(@PathVariable Long id,
                                @RequestParam("endDate") String endDateStr,
                                HttpSession session) {

        LocalDate today = LocalDate.now();
        LocalDate endDate;

        try {
            endDate = LocalDate.parse(endDateStr);
        } catch (Exception e) {
            return "redirect:/cart";
        }

        if (endDate.isBefore(today)) {
            endDate = today;
        }

        cartService.updateEndDate(id, endDate, session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        model.addAttribute("items", cartService.getAllItems(session));
        model.addAttribute("total", cartService.calculateTotal(session));
        model.addAttribute("today", LocalDate.now());
        return "checkout";
    }
}
