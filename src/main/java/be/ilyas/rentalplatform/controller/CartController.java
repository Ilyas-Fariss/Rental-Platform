package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.model.RentalOrder;
import be.ilyas.rentalplatform.repository.ProductRepository;
import be.ilyas.rentalplatform.repository.UserRepository;
import be.ilyas.rentalplatform.service.CartService;
import be.ilyas.rentalplatform.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductRepository productRepo;
    private final CartService cartService;
    private final UserRepository userRepo;
    private final OrderService orderService;

    public CartController(ProductRepository productRepo,
                          CartService cartService,
                          UserRepository userRepo,
                          OrderService orderService) {
        this.productRepo = productRepo;
        this.cartService = cartService;
        this.userRepo = userRepo;
        this.orderService = orderService;
    }

    // WINKELMAND BEKIJKEN
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> items = cartService.getAllItems(session);

        // we vullen twee namen zodat het zeker matcht met je Thymeleaf:
        model.addAttribute("items", items);
        model.addAttribute("cartItems", items);

        model.addAttribute("totalPrice", cartService.getTotalPrice(session));
        return "cart";
    }

    // PRODUCT TOEVOEGEN (accepteert zowel POST als GET – dan maakt het niet uit hoe je formulier staat)
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public String addToCart(@RequestParam("productId") Long productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Optional<Product> productOpt = productRepo.findById(productId);
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Product niet gevonden.");
            return "redirect:/catalog";
        }

        cartService.addProduct(session, productOpt.get());
        redirectAttributes.addFlashAttribute("success", "Product toegevoegd aan het mandje.");
        return "redirect:/catalog";
    }

    // PRODUCT VERWIJDEREN
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") Long productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        cartService.removeProduct(session, productId);
        redirectAttributes.addFlashAttribute("success", "Product verwijderd uit het mandje.");
        return "redirect:/cart";
    }

    // MANDJE LEEGMAKEN
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute("success", "Winkelmandje is leeggemaakt.");
        return "redirect:/cart";
    }

    // CHECKOUT PAGINA
    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        List<CartItem> items = cartService.getAllItems(session);

        model.addAttribute("items", items);
        model.addAttribute("cartItems", items);
        model.addAttribute("totalPrice", cartService.getTotalPrice(session));

        if (items.isEmpty()) {
            model.addAttribute("error", "Je winkelmandje is leeg.");
        }

        return "checkout";
    }

    // RESERVATIE BEVESTIGEN
    @PostMapping("/checkout/confirm")
    public String confirmCheckout(Authentication auth,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        if (auth == null) {
            return "redirect:/login";
        }

        Optional<AppUser> userOpt = userRepo.findByUsername(auth.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        AppUser user = userOpt.get();

        RentalOrder order = orderService.createOrderFromCart(user, session);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Je winkelmandje is leeg.");
            return "redirect:/cart";
        }

        redirectAttributes.addFlashAttribute("success", "Reservatie succesvol aangemaakt!");
        return "redirect:/orders";
    }
}
