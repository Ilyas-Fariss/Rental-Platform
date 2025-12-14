package be.ilyas.rentalplatform.config;

import be.ilyas.rentalplatform.model.Category;
import be.ilyas.rentalplatform.model.Product;
import be.ilyas.rentalplatform.repository.CategoryRepository;
import be.ilyas.rentalplatform.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // voorkom dubbel seeden
        if (productRepository.count() > 0) {
            return;
        }

        Category kabels = getOrCreateCategory("Kabels");
        Category belichting = getOrCreateCategory("Belichting");
        Category controlepanelen = getOrCreateCategory("Controlepanelen");

        List<Product> products = new ArrayList<>();

        // ================= KABELS =================
        products.add(makeProduct(
                "XLR-kabel 5m",
                "Professionele XLR-kabel van 5 meter voor microfoons en audio-installaties.",
                25,
                2.50,
                kabels
        ));
        products.add(makeProduct(
                "XLR-kabel 10m",
                "Professionele XLR-kabel van 10 meter voor grotere podia en opstellingen.",
                20,
                3.50,
                kabels
        ));
        products.add(makeProduct(
                "DMX-kabel 5m",
                "DMX-kabel van 5 meter voor lichtsturing en DMX-controllers.",
                20,
                3.00,
                kabels
        ));
        products.add(makeProduct(
                "DMX-kabel 10m",
                "DMX-kabel van 10 meter voor professionele lichtinstallaties.",
                15,
                4.00,
                kabels
        ));
        products.add(makeProduct(
                "Jack 6.3mm 3m",
                "Audio jack-kabel van 3 meter voor instrumenten en audioapparatuur.",
                30,
                1.80,
                kabels
        ));
        products.add(makeProduct(
                "Jack 6.3mm 6m",
                "Audio jack-kabel van 6 meter voor flexibele podiumopstellingen.",
                25,
                2.40,
                kabels
        ));
        products.add(makeProduct(
                "HDMI-kabel 10m",
                "HDMI-kabel van 10 meter voor projectoren en schermen.",
                15,
                3.90,
                kabels
        ));
        products.add(makeProduct(
                "Verlengkabel 10m",
                "Elektrische verlengkabel van 10 meter voor podium en studio.",
                20,
                2.20,
                kabels
        ));
        products.add(makeProduct(
                "Stekkerdoos 6-voudig",
                "Stekkerdoos met 6 aansluitingen voor stroomverdeling.",
                20,
                1.50,
                kabels
        ));
        products.add(makeProduct(
                "Adapter set (mini-jack/jack)",
                "Set audio-adapters voor verschillende jack-verbindingen.",
                30,
                1.00,
                kabels
        ));

        // ================= BELICHTING =================
        products.add(makeProduct(
                "LED PAR spot",
                "LED PAR spot geschikt voor podium- en sfeerverlichting.",
                10,
                12.00,
                belichting
        ));
        products.add(makeProduct(
                "Softbox lichtset",
                "Softbox lichtset voor film- en fotoproducties.",
                8,
                15.00,
                belichting
        ));
        products.add(makeProduct(
                "Fresnel spot 650W",
                "Professionele Fresnel spot van 650W voor theatergebruik.",
                6,
                18.00,
                belichting
        ));
        products.add(makeProduct(
                "Moving head",
                "Geautomatiseerde moving head voor dynamische lichtshows.",
                5,
                25.00,
                belichting
        ));
        products.add(makeProduct(
                "Lichtstatief",
                "Verstelbaar statief voor lampen en spots.",
                15,
                5.00,
                belichting
        ));
        products.add(makeProduct(
                "RGB LED strip kit",
                "RGB LED strip kit voor decoratieve verlichting.",
                12,
                8.00,
                belichting
        ));
        products.add(makeProduct(
                "Dimbare bouwlamp",
                "Krachtige bouwlamp met dimfunctie.",
                10,
                6.50,
                belichting
        ));
        products.add(makeProduct(
                "Gobo projector",
                "Projector voor logo’s en patronen met gobo’s.",
                4,
                22.00,
                belichting
        ));
        products.add(makeProduct(
                "Rookmachine",
                "Rookmachine voor lichteffecten op podium.",
                6,
                14.00,
                belichting
        ));
        products.add(makeProduct(
                "Klemmen (set)",
                "Set montageklemmen voor bevestiging van verlichting.",
                25,
                2.00,
                belichting
        ));

        // ================= CONTROLEPANELEN =================
        products.add(makeProduct(
                "DMX controller basic",
                "Basis DMX-controller voor eenvoudige lichtsturing.",
                6,
                10.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "DMX controller pro",
                "Geavanceerde DMX-controller voor professionele lichtshows.",
                4,
                18.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "Audio mixer 8-kanaals",
                "Audio mixer met 8 kanalen voor kleine producties.",
                5,
                16.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "Audio mixer 16-kanaals",
                "Audio mixer met 16 kanalen voor grotere producties.",
                3,
                24.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "MIDI controller",
                "MIDI controller voor muziekproductie en live performances.",
                6,
                12.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "Stream deck",
                "Stream deck met programmeerbare knoppen.",
                6,
                9.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "DI-box",
                "DI-box voor signaalomzetting en geluidskwaliteit.",
                12,
                4.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "Stagebox 16ch",
                "Stagebox met 16 kanalen voor audioverbindingen.",
                4,
                20.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "Monitor controller",
                "Controller voor monitor- en speakerbeheer.",
                5,
                11.00,
                controlepanelen
        ));
        products.add(makeProduct(
                "Power conditioner",
                "Power conditioner voor stabiele stroomvoorziening.",
                6,
                7.00,
                controlepanelen
        ));

        productRepository.saveAll(products);
    }

    private Category getOrCreateCategory(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> categoryRepository.save(new Category(name)));
    }

    private Product makeProduct(String name,
                                String description,
                                int stock,
                                double dailyPrice,
                                Category category) {

        return new Product(name, description, stock, dailyPrice, category);
    }
}
