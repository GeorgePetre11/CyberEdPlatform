package org.CyberEdPlatform.patterns.config;

import org.CyberEdPlatform.patterns.service.CartService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CartAdvice {
    
    private final CartService cartService;

    public CartAdvice(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartService")
    public CartService cartService() {
        return cartService;
    }
}
