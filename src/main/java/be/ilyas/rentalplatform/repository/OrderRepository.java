package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<RentalOrder, Long> {

    // Toon enkel de reservaties van de ingelogde user, nieuwste eerst
    List<RentalOrder> findByUserUsernameOrderByCreatedAtDesc(String username);
}
