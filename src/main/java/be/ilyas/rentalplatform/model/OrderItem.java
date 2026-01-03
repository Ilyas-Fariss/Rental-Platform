package be.ilyas.rentalplatform.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many order items belong to one order
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private RentalOrder order;

    // Many order items refer to one product
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    // ✅ nieuw: tot wanneer het product geleend wordt
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public OrderItem() {
        // JPA needs a default constructor
        this.endDate = LocalDate.now();
    }

    public OrderItem(RentalOrder order, Product product, int quantity, LocalDate endDate) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.endDate = (endDate != null) ? endDate : LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public RentalOrder getOrder() {
        return order;
    }

    public void setOrder(RentalOrder order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = (endDate != null) ? endDate : LocalDate.now();
    }
}
