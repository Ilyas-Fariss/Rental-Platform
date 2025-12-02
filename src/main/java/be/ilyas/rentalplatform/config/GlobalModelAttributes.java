package be.ilyas.rentalplatform.config;

import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@Component
public class GlobalModelAttributes {

    private final CartService cartService;

    public GlobalModelAttributes(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        List<CartItem> items = cartService.getAllItems(session);
        return (items == null) ? 0 : items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
