package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    public static final String SESSION_KEY = "CART_ITEMS";

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        Object attr = session.getAttribute(SESSION_KEY);
        if (attr == null) {
            List<CartItem> list = new ArrayList<>();
            session.setAttribute(SESSION_KEY, list);
            return list;
        }
        return (List<CartItem>) attr;
    }

    public List<CartItem> getAllItems(HttpSession session) {
        // kopie zodat de originele lijst in de sessie veilig blijft
        return new ArrayList<>(getCart(session));
    }

    public void addProduct(HttpSession session, Product product) {
        List<CartItem> cart = getCart(session);

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        cart.add(new CartItem(product, 1));
    }

    public void removeProduct(HttpSession session, Long productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }

    public int getCartCount(HttpSession session) {
        List<CartItem> cart = getCart(session);
        int total = 0;
        for (CartItem item : cart) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getTotalPrice(HttpSession session) {
        List<CartItem> cart = getCart(session);
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getQuantity() * item.getProduct().getDailyPrice();
        }
        return total;
    }
}
