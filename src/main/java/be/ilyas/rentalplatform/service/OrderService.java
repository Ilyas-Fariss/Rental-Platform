package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.*;
import be.ilyas.rentalplatform.repository.ProductRepository;
import be.ilyas.rentalplatform.repository.RentalOrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final RentalOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderService(RentalOrderRepository orderRepository,
                        ProductRepository productRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Transactional
    public RentalOrder createOrderFromCart(AppUser user, HttpSession session) {

        List<CartItem> cartItems = cartService.getAllItems(session);
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }

        RentalOrder order = new RentalOrder();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());

        double total = 0.0;
        int totalItems = 0;

        // vandaag (startdatum) – als jij later een startdatum toevoegt, pas je dit hier aan
        LocalDate today = LocalDate.now();

        for (CartItem ci : cartItems) {

            Product p = productRepository.findById(ci.getProduct().getId()).orElse(null);
            if (p == null) {
                continue;
            }

            int qty = ci.getQuantity();

            // 1) STOCK CHECK
            if (p.getStock() < qty) {
                throw new IllegalStateException("Not enough stock for: " + p.getName());
            }

            // 2) STOCK UPDATE
            p.setStock(p.getStock() - qty);
            productRepository.save(p);

            // 3) ORDER ITEM MAKEN
            OrderItem oi = new OrderItem();
            oi.setProduct(p);
            oi.setQuantity(qty);
            oi.setEndDate(ci.getEndDate()); // moet bestaan in CartItem, anders krijg je compile error
            oi.setOrder(order);

            order.getItems().add(oi);

            // 4) PRIJS BEREKENEN (prijs/dag * qty * dagen)
            LocalDate end = ci.getEndDate();
            if (end == null || end.isBefore(today)) {
                end = today; // fallback veilig
                oi.setEndDate(today);
            }

            long days = java.time.temporal.ChronoUnit.DAYS.between(today, end) + 1;
            double itemPrice = p.getDailyPrice() * qty * days;

            total += itemPrice;
            totalItems += qty;
        }

        order.setTotalPrice(total);
        order.setTotalItems(totalItems);

        // 5) ORDER OPSLAAN (incl items door cascade)
        RentalOrder saved = orderRepository.save(order);

        // 6) CART LEEGMAKEN
        cartService.clearCart(session);

        return saved;
    }

    public List<RentalOrder> getOrdersForUser(AppUser user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public RentalOrder getOrderForUser(Long orderId, AppUser user) {
        return orderRepository.findByIdAndUser(orderId, user).orElse(null);
    }
}
