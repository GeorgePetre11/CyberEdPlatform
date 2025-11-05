// DESIGN PATTERN: SINGLETON PATTERN (Implicit via Spring Framework)
// Locations: All @Service, @Configuration, @Controller classes
//
// Description: Spring Framework manages beans as singletons by default. All classes
// annotated with @Service, @Configuration, @Component, @Controller, etc. are created
// once and reused throughout the application lifecycle.
//
// Pattern Elements:
// - Singleton Instance: Spring IoC Container manages single instance
// - Creation: Spring creates instances at startup (or first use if lazy)
// - Access: Instances injected via dependency injection
// - Thread Safety: Managed by Spring Framework
//
// Note: This is NOT an explicit Singleton implementation (no private constructor,
// no static getInstance method). Instead, Spring provides singleton behavior
// through its Inversion of Control (IoC) container.

package org.example.cybersecurity_platform.examples;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

/**
 * SINGLETON PATTERN: Spring-Managed Singletons
 * 
 * In traditional Singleton pattern, you would see:
 * 
 * public class Singleton {
 *     private static Singleton instance;
 *     private Singleton() {}  // private constructor
 *     public static Singleton getInstance() {
 *         if (instance == null) {
 *             instance = new Singleton();
 *         }
 *         return instance;
 *     }
 * }
 * 
 * However, Spring provides singleton behavior automatically through
 * dependency injection and its IoC container.
 */

// EXAMPLE 1: Service Singleton
// The CustomUserDetailsService is a singleton - only one instance exists
@Service
class CustomUserDetailsServiceExample {
    // Spring creates ONE instance of this class
    // Every component that depends on this service gets the SAME instance
    
    public String doSomething() {
        return "I am a singleton!";
    }
}

// EXAMPLE 2: Configuration Singleton  
// SecurityConfig is a singleton that configures the application
@Configuration
class SecurityConfigExample {
    // Spring creates ONE instance of this configuration class
    // All @Bean methods are called on the SAME instance
}

// EXAMPLE 3: Controller Singleton
// All controllers are singletons by default
@Controller
class HomeControllerExample {
    // Spring creates ONE instance that handles all requests
    // Multiple concurrent requests are handled by the same controller instance
    // (but in different threads - Spring handles thread safety)
}

// EXAMPLE 4: Exception - Session Scope (NOT a Singleton)
// CartService is NOT a singleton because it's session-scoped
@Service
@SessionScope
class CartServiceExample {
    // This is NOT a singleton - one instance PER HTTP SESSION
    // This is an exception to Spring's default singleton behavior
    
    // Each user gets their own CartService instance for their session
}

/**
 * BENEFITS OF SPRING'S SINGLETON MANAGEMENT:
 * 
 * 1. Memory Efficiency: Only one instance of each service/controller
 * 2. Consistent State: Same instance used throughout application
 * 3. Easy Dependency Injection: Spring handles instance management
 * 4. Thread Safety: Spring manages concurrent access
 * 5. Lifecycle Management: Spring handles creation and destruction
 * 
 * REAL EXAMPLES IN OUR PROJECT:
 * 
 * @Service CartService          ← Session-scoped (exception)
 * @Service CustomUserDetailsService  ← Singleton
 * @Configuration SecurityConfig      ← Singleton
 * @Controller HomeController         ← Singleton
 * @Controller CourseController       ← Singleton
 * @Controller CartController         ← Singleton
 * 
 * All of these (except CartService) are singletons managed by Spring.
 */
