package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
