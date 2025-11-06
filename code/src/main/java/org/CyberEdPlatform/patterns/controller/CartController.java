package org.CyberEdPlatform.patterns.controller;

import org.CyberEdPlatform.patterns.model.Purchase;
import org.CyberEdPlatform.patterns.model.User;
import org.CyberEdPlatform.patterns.repository.PurchaseRepository;
import org.CyberEdPlatform.patterns.repository.UserRepository;
import org.CyberEdPlatform.patterns.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;

    public CartController(CartService cartService, 
                         PurchaseRepository purchaseRepository,
                         UserRepository userRepository) {
        this.cartService = cartService;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        System.out.println("[SINGLETON] CartController instance created");
        System.out.println("   -> Injected CartService (FACADE) - Session-scoped");
    }

    @GetMapping
    public String viewCart(Model model) {
        System.out.println("[SINGLETON] CartController viewing cart");
        System.out.println("[FACADE] Using CartService.getItems() - hides complexity");
        
        model.addAttribute("cartItems", cartService.getItems());
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id) {
        System.out.println("[SINGLETON] CartController adding to cart");
        System.out.println("[FACADE] Using CartService.add() - simple interface");
        
        cartService.add(id);
        return "redirect:/courses";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        System.out.println("[FACADE] Using CartService.remove()");
        cartService.remove(id);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("[SINGLETON] CartController processing checkout");
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("[FACADE] Using CartService.getItems() and getTotalPrice()");
        
        cartService.getItems().forEach((course, qty) -> {
            for (int i = 0; i < qty; i++) {
                Purchase purchase = new Purchase(user, course);
                purchaseRepository.save(purchase);
                System.out.println("[OBSERVER] Purchase saved - @CreationTimestamp auto-set");
            }
            
            course.setQuantity(course.getQuantity() - qty);
        });

        System.out.println("[FACADE] Using CartService.clear() to empty cart");
        cartService.clear();
        
        return "redirect:/";
    }
}
