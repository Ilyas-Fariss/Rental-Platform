package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "CART_ITEMS";

    @SuppressWarnings("unchecked")
    private List<CartItem> getOrCreateCart(HttpSession session) {
        Object obj = session.getAttribute(CART_SESSION_KEY);

        if (obj == null) {
            List<CartItem> newCart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, newCart);
            return newCart;
        }

        return (List<CartItem>) obj;
    }

    public List<CartItem> getAllItems(HttpSession session) {
        return getOrCreateCart(session);
    }

    public void addToCart(Product product, HttpSession session) {
        List<CartItem> cart = getOrCreateCart(session);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        cart.add(new CartItem(product, 1));
    }

    public void removeFromCart(Long productId, HttpSession session) {
        List<CartItem> cart = getOrCreateCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    // ✅ nieuw: endDate updaten per product in cart
    public void updateEndDate(Long productId, LocalDate endDate, HttpSession session) {
        List<CartItem> cart = getOrCreateCart(session);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                item.setEndDate(endDate);
                return;
            }
        }
    }

    // ✅ aantal dagen (vandaag -> endDate) inclusief
    private long getDays(LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (endDate == null) return 1;

        long days = ChronoUnit.DAYS.between(today, endDate) + 1; // inclusief
        return Math.max(days, 1);
    }

    // ✅ totaal = dailyPrice * quantity * dagen
    public double calculateTotal(HttpSession session) {
        List<CartItem> cart = getOrCreateCart(session);

        double total = 0.0;
        for (CartItem item : cart) {
            long days = getDays(item.getEndDate());
            total += item.getProduct().getDailyPrice() * item.getQuantity() * days;
        }
        return total;
    }

    public int getCartCount(HttpSession session) {
        List<CartItem> cart = getOrCreateCart(session);
        return cart.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void clearCart(HttpSession session) {
        session.setAttribute("cart", new java.util.ArrayList<>());
    }

}
