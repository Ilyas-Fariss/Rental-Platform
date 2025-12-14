package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.OrderItem;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.model.RentalOrder;
import be.ilyas.rentalplatform.repository.RentalOrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public RentalOrder createOrderFromCart(AppUser user, HttpSession session) {

        List<CartItem> cartItems = cartService.getAllItems(session);

        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }

        RentalOrder order = new RentalOrder();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;
        int totalItems = 0;

        LocalDate today = LocalDate.now();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(quantity);

            // als je endDate in CartItem hebt:
            oi.setEndDate(cartItem.getEndDate());

            // voeg toe aan order (BELANGRIJK: order.getItems() mag niet null zijn)
            order.getItems().add(oi);

            // prijs: dailyPrice * quantity * days
            if (product != null && cartItem.getEndDate() != null) {
                long days = ChronoUnit.DAYS.between(today, cartItem.getEndDate()) + 1;
                if (days < 1) days = 1;
                total += product.getDailyPrice() * quantity * days;
            } else if (product != null) {
                // fallback: als geen datum gekozen werd, reken 1 dag
                total += product.getDailyPrice() * quantity;
            }

            totalItems += quantity;
        }

        order.setTotalItems(totalItems);
        order.setTotalPrice(total);

        RentalOrder saved = rentalOrderRepository.save(order);

        cartService.clearCart(session);

        return saved;
    }

    public List<RentalOrder> getOrdersForUser(AppUser user) {
        return rentalOrderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public RentalOrder getOrderForUser(Long orderId, AppUser user) {
        return rentalOrderRepository.findByIdAndUser(orderId, user).orElse(null);
    }
}
