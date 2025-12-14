package be.ilyas.rentalplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Dit zoekt naar src/main/resources/templates/dashboard.html
        return "dashboard";
    }

    // Optioneel: /dashboard werkt ook rechtstreeks
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
