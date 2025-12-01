package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
