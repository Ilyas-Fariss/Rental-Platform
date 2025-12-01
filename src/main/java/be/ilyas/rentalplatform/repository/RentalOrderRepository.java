package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {

    // Alle orders van een user, nieuwst eerst
    List<RentalOrder> findByUserOrderByCreatedAtDesc(AppUser user);
}
