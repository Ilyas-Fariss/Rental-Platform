package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.repository.RentalOrderRepository;
import be.ilyas.rentalplatform.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CartService cartService;
    private final RentalOrderRepository rentalOrderRepository;

    public HomeController(CartService cartService, RentalOrderRepository rentalOrderRepository) {
        this.cartService = cartService;
        this.rentalOrderRepository = rentalOrderRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@AuthenticationPrincipal AppUser user,
                            HttpSession session,
                            Model model) {

        // cartCount (zodat je navbar + card altijd klopt)
        int cartCount = cartService.getCartCount(session);
        model.addAttribute("cartCount", cartCount);

        // ✅ ordersCount correct voor deze user
        long ordersCount = 0;
        if (user != null) {
            ordersCount = rentalOrderRepository.countByUser(user);
        }
        model.addAttribute("ordersCount", ordersCount);

        return "dashboard";
    }
}
