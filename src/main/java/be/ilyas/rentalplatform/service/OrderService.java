package be.ilyas.rentalplatform.service;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.OrderItem;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.model.RentalOrder;
import be.ilyas.rentalplatform.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public OrderService(RentalOrderRepository rentalOrderRepository,
                        CartService cartService,
                        ProductRepository productRepository) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }

    /**
     * Maakt een nieuwe order op basis van het huidige winkelmandje in de sessie.
     * - Maakt RentalOrder + OrderItems
     * - Past de stock aan van de producten
     * - Leegt daarna het mandje
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
            Product product = cartItem.getProduct();

            // OrderItem aanmaken
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtTime(product.getDailyPrice());

            orderItems.add(orderItem);

            totalItems += cartItem.getQuantity();
            totalPrice += cartItem.getQuantity() * product.getDailyPrice();

            // Stock aanpassen (heel basic, zonder extra checks)
            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                newStock = 0;
            }
            product.setStock(newStock);
            productRepository.save(product);
        }

        order.setItems(orderItems);
        order.setTotalItems(totalItems);
        order.setTotalPrice(totalPrice);

        RentalOrder savedOrder = rentalOrderRepository.save(order);

        // mandje leegmaken
        cartService.clearCart(session);

        return savedOrder;
    }

    public List<RentalOrder> getOrdersForUser(AppUser user) {
        return rentalOrderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public RentalOrder getOrderForUser(Long orderId, AppUser user) {
        return rentalOrderRepository.findById(orderId)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .orElse(null);
    }
}
