package be.ilyas.rentalplatform.repository;

import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);
}
