package be.ilyas.rentalplatform.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Elke cart-item hoort bij 1 user
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;

    // En verwijst naar 1 product
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDate endDate;

    public CartItem() {
    }

    public CartItem(AppUser user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.endDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
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
        this.endDate = (endDate == null) ? LocalDate.now() : endDate;
    }
}
