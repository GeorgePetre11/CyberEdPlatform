package org.CyberEdPlatform.patterns.controller;

import org.CyberEdPlatform.patterns.model.Role;
import org.CyberEdPlatform.patterns.model.User;
import org.CyberEdPlatform.patterns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        System.out.println("[SINGLETON] AuthController instance created");
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        System.out.println("[SINGLETON] AuthController handling registration");
        
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        
        System.out.println("[STRATEGY] Encoding password with current strategy");
        user.setPassword(passwordEncoder.encode(password));
        
        user.setRoles(Set.of(Role.ROLE_USER));

        userRepository.save(user);
        System.out.println("User registered successfully: " + username);
        
        return "redirect:/login?registered";
    }
}
