package be.ilyas.rentalplatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String username = auth != null ? auth.getName() : "gast";
        model.addAttribute("username", username);
        return "dashboard";
    }
}
