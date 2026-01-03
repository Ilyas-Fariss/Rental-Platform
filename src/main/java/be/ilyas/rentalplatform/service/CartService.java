package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.CartItemRepository;
import be.ilyas.rentalplatform.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;

    public CartService(CartItemRepository cartItemRepo, UserRepository userRepo) {
        this.cartItemRepo = cartItemRepo;
        this.userRepo = userRepo;
    }

    // We houden HttpSession in de method signatures zodat je controller niet moet wijzigen,
    // maar we gebruiken de session niet meer om de cart op te slaan.

    private AppUser getCurrentUserOrNull() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String identifier = auth.getName();

        if (identifier == null || identifier.equalsIgnoreCase("anonymousUser")) {
            return null;
        }

        var userOpt = userRepo.findByUsername(identifier);

        if (userOpt.isEmpty()) {
            userOpt = userRepo.findByEmail(identifier);
        }

        return userOpt.orElse(null);
    }

    public List<CartItem> getAllItems(HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null) return List.of();
        return cartItemRepo.findByUser(user);
    }

    public void addToCart(Product product, HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null || product == null) return;

        CartItem item = cartItemRepo.findByUserAndProduct(user, product).orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepo.save(item);
            return;
        }

        cartItemRepo.save(new CartItem(user, product, 1));
    }

    public void removeFromCart(Long productId, HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null || productId == null) return;

        cartItemRepo.deleteByUserAndProduct_Id(user, productId);
    }

    public void updateEndDate(Long productId, LocalDate endDate, HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null || productId == null) return;

        List<CartItem> items = cartItemRepo.findByUser(user);
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                item.setEndDate(endDate);
                cartItemRepo.save(item);
                return;
            }
        }
    }

    private long getDays(LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (endDate == null) return 1;

        long days = ChronoUnit.DAYS.between(today, endDate) + 1;
        return Math.max(days, 1);
    }

    public double calculateTotal(HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null) return 0.0;

        var cart = cartItemRepo.findByUser(user);

        double total = 0.0;
        for (CartItem item : cart) {
            long days = getDays(item.getEndDate());
            total += item.getProduct().getDailyPrice() * item.getQuantity() * days;
        }
        return total;
    }

    public int getCartCount(HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null) return 0;

        return cartItemRepo.findByUser(user)
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void clearCart(HttpSession session) {
        AppUser user = getCurrentUserOrNull();
        if (user == null) return;

        cartItemRepo.deleteByUser(user);
    }
}
