package be.ilyas.rentalplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Zorgt ervoor dat /uploads/** verwijst naar de "uploads" map in je projectroot
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
