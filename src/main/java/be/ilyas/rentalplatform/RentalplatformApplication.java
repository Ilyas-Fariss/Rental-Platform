package be.ilyas.rentalplatform;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.model.Category;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.CategoryRepository;
import be.ilyas.rentalplatform.repository.ProductRepository;
import be.ilyas.rentalplatform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RentalplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalplatformApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(CategoryRepository categoryRepo,
							   ProductRepository productRepo,
							   UserRepository userRepo,
							   PasswordEncoder passwordEncoder) {
		return args -> {

			// Demo user
			if (userRepo.findByUsername("testuser") == null) {
				AppUser u = new AppUser("testuser",
						"testuser@student.ehb.be",
						passwordEncoder.encode("Password!123"),
						"ROLE_USER");
				userRepo.save(u);
			}

			// Alleen vullen als er nog geen producten zijn
			if (productRepo.count() > 0) {
				return;
			}

			// Categorieën
			Category cables = new Category("Kabels");
			Category lights = new Category("Belichting");
			Category controllers = new Category("Controlepanelen");

			categoryRepo.save(cables);
			categoryRepo.save(lights);
			categoryRepo.save(controllers);

			// Helper om product aan te maken
			java.util.function.BiConsumer<Product, Category> saveWithCategory = (p, c) -> {
				p.setCategory(c);
				productRepo.save(p);
			};

			// --- Kabels (10 stuks) ---
			saveWithCategory.accept(newProduct("XLR-kabel 10m",
					"Degelijke XLR-kabel voor audio.",
					2.5, 15), cables);
			saveWithCategory.accept(newProduct("XLR-kabel 5m",
					"Kortere XLR-kabel voor kleine setups.",
					2.0, 20), cables);
			saveWithCategory.accept(newProduct("Speakon-kabel 5m",
					"Kabel voor luidsprekers en versterkers.",
					2.5, 12), cables);
			saveWithCategory.accept(newProduct("Jack 6.3mm 3m",
					"Instrumentkabel voor gitaar of keyboard.",
					1.8, 25), cables);
			saveWithCategory.accept(newProduct("DMX-kabel 3m",
					"DMX-kabel voor lichtsturing.",
					2.2, 18), cables);
			saveWithCategory.accept(newProduct("DMX-kabel 10m",
					"Lange DMX-kabel voor grotere podia.",
					3.0, 10), cables);
			saveWithCategory.accept(newProduct("PowerCON-kabel 3m",
					"Stroomkabel met PowerCON-connector.",
					3.0, 10), cables);
			saveWithCategory.accept(newProduct("IEC-stroomkabel 2m",
					"Standaard stroomkabel voor toestellen.",
					1.2, 40), cables);
			saveWithCategory.accept(newProduct("RJ45-netwerkkabel 5m",
					"Netwerkkabel voor Dante of netwerkapparatuur.",
					1.5, 30), cables);
			saveWithCategory.accept(newProduct("Mini-jack naar RCA",
					"Kabel om laptop of smartphone te verbinden met mengpaneel.",
					1.5, 20), cables);

			// --- Belichting (10 stuks) ---
			saveWithCategory.accept(newProduct("LED Spot 50W",
					"Aanpasbare LED spot voor podium.",
					7.5, 8), lights);
			saveWithCategory.accept(newProduct("LED Bar RGB",
					"Kleurvolle LED-bar voor sfeerverlichting.",
					9.0, 6), lights);
			saveWithCategory.accept(newProduct("PAR64 Halogeen",
					"Klassieke PAR64-lamp met warm licht.",
					6.0, 10), lights);
			saveWithCategory.accept(newProduct("Moving Head Beam",
					"Snelle bewegende beam-spot.",
					25.0, 4), lights);
			saveWithCategory.accept(newProduct("Moving Head Wash",
					"Breed uitwaaierende wash-spot.",
					27.0, 4), lights);
			saveWithCategory.accept(newProduct("Blinder 2x650W",
					"Publieksblinder voor sterke effecten.",
					12.0, 6), lights);
			saveWithCategory.accept(newProduct("Stroboscoop 1500W",
					"Krachtige stroboscoop voor flitseffecten.",
					15.0, 3), lights);
			saveWithCategory.accept(newProduct("UV Blacklight Tube",
					"UV-lamp voor speciale effecten.",
					5.0, 10), lights);
			saveWithCategory.accept(newProduct("LED Fresnel",
					"Zachte spot met fresnellens.",
					11.0, 6), lights);
			saveWithCategory.accept(newProduct("Battery LED Uplight",
					"Draadloze uplight voor decoratieve verlichting.",
					13.0, 8), lights);

			// --- Controlepanelen (10 stuks) ---
			saveWithCategory.accept(newProduct("DMX-controller",
					"Basiscontroller voor lichtsturing.",
					15.0, 3), controllers);
			saveWithCategory.accept(newProduct("Lichtconsole 24 kanalen",
					"Eenvoudig lichtpaneel met 24 kanalen.",
					18.0, 4), controllers);
			saveWithCategory.accept(newProduct("Lichtconsole 48 kanalen",
					"Uitgebreid lichtpaneel met submasters.",
					22.0, 3), controllers);
			saveWithCategory.accept(newProduct("DMX-node ArtNet",
					"Netwerk-node voor ArtNet/DMX.",
					14.0, 5), controllers);
			saveWithCategory.accept(newProduct("Showcontroller Pro",
					"Geavanceerde showcontroller met timecode.",
					30.0, 2), controllers);
			saveWithCategory.accept(newProduct("MIDI-naar-DMX interface",
					"Interface om DMX via MIDI te sturen.",
					10.0, 4), controllers);
			saveWithCategory.accept(newProduct("DMX-splitter 4x",
					"Verdeelt één DMX-lijn naar vier uitgangen.",
					8.0, 6), controllers);
			saveWithCategory.accept(newProduct("Wireless DMX zender",
					"Draadloze DMX-zender.",
					9.5, 5), controllers);
			saveWithCategory.accept(newProduct("Wireless DMX ontvanger",
					"Draadloze DMX-ontvanger.",
					9.5, 5), controllers);
			saveWithCategory.accept(newProduct("Touchscreen Lichtcontroller",
					"Compact touchscreenpaneel voor kleinere setups.",
					20.0, 2), controllers);
		};
	}

	private static Product newProduct(String name, String description,
									  double dailyPrice, int stock) {
		Product p = new Product();
		p.setName(name);
		p.setDescription(description);
		p.setDailyPrice(dailyPrice);
		p.setStock(stock);
		return p;
	}
}
