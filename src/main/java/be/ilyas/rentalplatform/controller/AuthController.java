package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Gebruikersnaam bestaat al.");
            return "register";
        }

        String encodedPassword = passwordEncoder.encode(password);

        AppUser user = new AppUser(username, email, encodedPassword, "ROLE_USER");
        userRepository.save(user);

        // na registratie naar login
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
