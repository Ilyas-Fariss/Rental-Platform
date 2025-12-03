package be.ilyas.rentalplatform.controller;

import be.ilyas.rentalplatform.model.AppUser;
import be.ilyas.rentalplatform.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private AppUser getCurrentUser(Authentication auth) {
        if (auth == null) {
            return null;
        }
        Optional<AppUser> userOpt = userRepo.findByUsername(auth.getName());
        return userOpt.orElse(null);
    }

    @GetMapping("/profile")
    public String showProfile(Authentication auth, Model model) {
        AppUser user = getCurrentUser(auth);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user_profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication auth,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String dateOfBirth,
                                RedirectAttributes redirectAttributes) {

        AppUser user = getCurrentUser(auth);
        if (user == null) {
            return "redirect:/login";
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (dateOfBirth != null && !dateOfBirth.isBlank()) {
            user.setDateOfBirth(LocalDate.parse(dateOfBirth));
        } else {
            user.setDateOfBirth(null);
        }

        userRepo.save(user);

        // >>> hier komt je bevestiging
        redirectAttributes.addFlashAttribute("success", "Je profiel is succesvol bijgewerkt.");

        return "redirect:/profile";
    }

    @PostMapping("/profile/upload-image")
    public String updateProfileImage(Authentication auth,
                                     @RequestParam("profileImage") MultipartFile file,
                                     RedirectAttributes redirectAttributes) throws IOException {

        AppUser user = getCurrentUser(auth);
        if (user == null) {
            return "redirect:/login";
        }

        if (file != null && !file.isEmpty()) {
            // eenvoudige opslag in lokale folder "uploads/profile"
            String uploadDir = "uploads/profile";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName = user.getId() + "_" + file.getOriginalFilename();
            File dest = new File(uploadFolder, fileName);
            file.transferTo(dest);

            user.setProfileImage(fileName);
            userRepo.save(user);

            redirectAttributes.addFlashAttribute("success", "Je profielfoto is succesvol geüpdatet.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Geen bestand geselecteerd.");
        }

        return "redirect:/profile";
    }
}
