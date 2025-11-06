package org.CyberEdPlatform.patterns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public HomeController() {
        System.out.println("[SINGLETON] HomeController instance created");
    }

    @GetMapping("/")
    public String home() {
        System.out.println("[SINGLETON] HomeController handling request (same instance for all users)");
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
