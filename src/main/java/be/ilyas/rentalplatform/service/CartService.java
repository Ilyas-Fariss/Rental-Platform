package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cartItems";

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(Product product, HttpSession session) {
        List<CartItem> cart = getCart(session);

        // check of product al bestaat in het mandje
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        CartItem newItem = new CartItem(product, 1);
        cart.add(newItem);
    }

    public List<CartItem> getAllItems(HttpSession session) {
        return getCart(session);
    }

    public void removeItem(Long productId, HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart(HttpSession session) {
        session.setAttribute(CART_SESSION_KEY, new ArrayList<>());
    }
}
