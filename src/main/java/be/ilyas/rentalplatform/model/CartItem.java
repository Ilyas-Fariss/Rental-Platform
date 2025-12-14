package be.ilyas.rentalplatform.model;

import java.time.LocalDate;

public class CartItem {

    private Product product;
    private int quantity;

    // ✅ nieuw: tot wanneer lenen
    private LocalDate endDate;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.endDate = LocalDate.now(); // default = vandaag (1 dag)
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
        this.endDate = endDate;
    }
}
