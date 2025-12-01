package be.ilyas.rentalplatform;

import be.ilyas.rentalplatform.model.Category;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.CategoryRepository;
import be.ilyas.rentalplatform.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RentalplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalplatformApplication.class, args);
	}

	@Bean
	CommandLineRunner dataLoader(CategoryRepository categoryRepository,
								 ProductRepository productRepository) {
		return args -> {
			if (categoryRepository.count() == 0 && productRepository.count() == 0) {

				Category cables = new Category("Kabels");
				Category lights = new Category("Belichting");
				Category panels = new Category("Controlepanelen");

				categoryRepository.save(cables);
				categoryRepository.save(lights);
				categoryRepository.save(panels);

				productRepository.save(new Product(
						"XLR-kabel 10m",
						"Degelijke XLR-kabel voor audio",
						15,
						2.5,
						cables
				));

				productRepository.save(new Product(
						"LED Spot 50W",
						"Aanpasbare LED spot voor podium",
						8,
						7.5,
						lights
				));

				productRepository.save(new Product(
						"DMX-controller",
						"Controller voor lichtsturing",
						3,
						15.0,
						panels
				));
			}
		};
	}
}
