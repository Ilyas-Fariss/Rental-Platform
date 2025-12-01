package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String SESSION_KEY = "CART_ITEMS";

    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(SESSION_KEY, cart);
        }
        return cart;
    }

    public List<CartItem> getAllItems(HttpSession session) {
        return getCart(session);
    }

    public void addToCart(Product product, HttpSession session) {
        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        cart.add(new CartItem(product, 1));
    }

    public void removeFromCart(Long productId, HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart(HttpSession session) {
        session.setAttribute(SESSION_KEY, new ArrayList<>());
    }

    public double calculateTotal(HttpSession session) {
        return getCart(session)
                .stream()
                .mapToDouble(item -> item.getProduct().getDailyPrice() * item.getQuantity())
                .sum();
    }
}
