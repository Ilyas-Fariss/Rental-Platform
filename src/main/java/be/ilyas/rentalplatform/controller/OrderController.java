package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.RentalOrder;
import be.ilyas.rentalplatform.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Wordt aangeroepen vanuit de checkout-knop
    @GetMapping("/orders/confirm")
    public String confirmOrder(@AuthenticationPrincipal AppUser user,
                               HttpSession session) {

        RentalOrder order = orderService.createOrderFromCart(user, session);

        if (order == null) {
            // Niets in het mandje → terug naar mandje
            return "redirect:/cart";
        }

        // Na bevestiging sturen we naar de detailpagina van deze reservatie
        return "redirect:/orders/" + order.getId();
    }

    // Overzicht van alle reservaties van de ingelogde gebruiker
    @GetMapping("/orders")
    public String listOrders(@AuthenticationPrincipal AppUser user,
                             Model model) {

        List<RentalOrder> orders = orderService.getOrdersForUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }

    // Detailpagina van één reservatie
    @GetMapping("/orders/{orderId}")
    public String orderDetails(@PathVariable Long orderId,
                               @AuthenticationPrincipal AppUser user,
                               Model model) {

        RentalOrder order = orderService.getOrderForUser(orderId, user);
        if (order == null) {
            // Bestaat niet of is niet van deze user
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order_details";
    }
}
