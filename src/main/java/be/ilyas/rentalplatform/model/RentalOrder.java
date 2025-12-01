package be.ilyas.rentalplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class RentalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private double totalPrice;

    private int totalItems;

    @ManyToOne
    private AppUser user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    public RentalOrder() {}

    // GETTERS

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public AppUser getUser() {
        return user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // SETTERS

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;

        if (items != null) {
            for (OrderItem oi : items) {
                oi.setOrder(this);
            }
        }
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
