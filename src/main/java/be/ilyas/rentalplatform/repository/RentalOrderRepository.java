package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {

    List<RentalOrder> findByUserOrderByCreatedAtDesc(AppUser user);

    Optional<RentalOrder> findByIdAndUser(Long id, AppUser user);
}
