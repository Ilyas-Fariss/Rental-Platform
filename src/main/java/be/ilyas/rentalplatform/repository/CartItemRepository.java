package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.CartItem;
import be.ilyas.rentalplatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(AppUser user);
    Optional<CartItem> findByUserAndProduct(AppUser user, Product product);
    void deleteByUser(AppUser user);
    void deleteByUserAndProduct_Id(AppUser user, Long productId);
}
