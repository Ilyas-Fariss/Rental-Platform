package be.ilyas.rentalplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String showProfile() {
        // Zoekt naar src/main/resources/templates/profile.html
        return "profile";
    }
}
