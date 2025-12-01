package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.OrderItem;
import be.ilyas.rentalplatform.model.RentalOrder;
import be.ilyas.rentalplatform.repository.RentalOrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final RentalOrderRepository rentalOrderRepository;
    private final CartService cartService;

    public OrderService(RentalOrderRepository rentalOrderRepository,
                        CartService cartService) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.cartService = cartService;
    }

    /**
     * Maakt een nieuwe order op basis van het winkelmandje in de sessie.
     * Leegt daarna het mandje.
     */
    public RentalOrder createOrderFromCart(AppUser user, HttpSession session) {

        List<CartItem> cartItems = cartService.getAllItems(session);

        if (cartItems == null || cartItems.isEmpty()) {
            return null; // niets te bestellen
        }

        RentalOrder order = new RentalOrder();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        int totalItems = 0;
        double totalPrice = 0.0;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getDailyPrice()
            );
            orderItems.add(orderItem);

            totalItems += cartItem.getQuantity();
            totalPrice += cartItem.getQuantity() * cartItem.getProduct().getDailyPrice();
        }

        order.setItems(orderItems);
        order.setTotalItems(totalItems);
        order.setTotalPrice(totalPrice);

        RentalOrder savedOrder = rentalOrderRepository.save(order);

        // mandje leegmaken
        cartService.clearCart(session);

        return savedOrder;
    }

    /**
     * Haal alle orders van een gebruiker op.
     */
    public List<RentalOrder> getOrdersForUser(AppUser user) {
        return rentalOrderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Haal één specifieke order op voor deze gebruiker.
     */
    public RentalOrder getOrderForUser(Long orderId, AppUser user) {
        return rentalOrderRepository.findById(orderId)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .orElse(null);
    }
}
