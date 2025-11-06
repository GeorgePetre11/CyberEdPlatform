# Design Patterns Implementation Documentation

## Project Overview

This standalone Spring Boot application demonstrates **6 fundamental design patterns** extracted from the Cybersecurity Learning Platform. Each pattern is fully functional, well-documented, and can be tested in isolation.

---

## Architecture Summary

```
design-patterns-demo/
├── src/main/java/org/example/patterns/
│   ├── DesignPatternsDemoApplication.java    [Factory Method + Singleton]
│   ├── model/
│   │   ├── User.java                         [Used by all patterns]
│   │   ├── Course.java                       [Observer Pattern]
│   │   └── Purchase.java                     [Observer Pattern]
│   ├── repository/                           [Data Access Layer]
│   │   ├── UserRepository.java
│   │   ├── CourseRepository.java
│   │   └── PurchaseRepository.java
│   ├── service/
│   │   ├── CartService.java                  [Facade Pattern]
│   │   └── CustomUserDetailsService.java     [Template Method Pattern]
│   ├── config/
│   │   ├── SecurityConfig.java               [Strategy Pattern]
│   │   └── CartAdvice.java
│   └── controller/                           [All Singleton Pattern]
│       ├── HomeController.java
│       ├── AuthController.java
│       ├── CourseController.java
│       ├── CartController.java
│       └── AdminController.java
└── src/main/resources/
    ├── templates/                            [Thymeleaf views]
    │   ├── home.html
    │   ├── login.html
    │   ├── register.html
    │   ├── courses.html
    │   ├── cart.html
    │   └── admin-courses.html
    └── application.properties
```

---

## Design Patterns Detailed

### 1. 🎭 Facade Pattern

**Location:** `service/CartService.java`

**Purpose:** Simplifies complex shopping cart operations by providing a unified interface.

**Implementation:**
```java
@Service
@SessionScope
public class CartService {
    private final CourseRepository courseRepo;
    private final Map<Long, Integer> items = new LinkedHashMap<>();
    
    public Map<Course, Integer> getItems() { /* complex logic */ }
    public void add(Long courseId) { /* simple interface */ }
    public void remove(Long courseId) { /* simple interface */ }
    public void clear() { /* simple interface */ }
    public double getTotalPrice() { /* calculation logic */ }
}
```

**Without Facade:**
```java
// Controller would need:
Map<Long, Integer> cartItems = session.getAttribute("cart");
double total = 0.0;
for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
    Course course = courseRepo.findById(entry.getKey()).orElse(null);
    if (course != null) {
        total += course.getPrice() * entry.getValue();
    }
}
// ... 20+ more lines
```

**With Facade:**
```java
// Controller simply calls:
double total = cartService.getTotalPrice();
```

**Benefits:**
- Reduces controller complexity from 50+ lines to 5 lines
- Centralizes cart logic
- Easy to add features (discounts, taxes, etc.)
- Easy to test (mock one service vs multiple repositories)
- Can swap implementation (e.g., Redis cache) without affecting controllers

**Test:** Add items to cart, view cart, checkout - observe console logs

---

### 2. 🔄 Strategy Pattern

**Location:** `config/SecurityConfig.java`

**Purpose:** Allows interchangeable password hashing algorithms without changing client code.

**Implementation:**
```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Current strategy
        // return new Argon2PasswordEncoder(); // Alternative
        // return new SCryptPasswordEncoder();  // Alternative
    }
}
```

**Strategy Interface:**
```java
public interface PasswordEncoder {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
```

**Concrete Strategies:**
- `BCryptPasswordEncoder` - Adaptive hashing, industry standard
- `Argon2PasswordEncoder` - Memory-hard, GPU-resistant
- `SCryptPasswordEncoder` - CPU and memory intensive
- `Pbkdf2PasswordEncoder` - PBKDF2 with HMAC

**Client Code (Never Changes):**
```java
@Controller
public class AuthController {
    private final PasswordEncoder passwordEncoder; // Interface!
    
    @PostMapping("/register")
    public String register(String password) {
        user.setPassword(passwordEncoder.encode(password)); // Strategy used here
    }
}
```

**Benefits:**
- Switch algorithms by changing ONE line
- Support multiple encoders during migration
- Client code depends on interface, not implementation
- Easy to add new algorithms

**Test:** Register user, switch strategy in `SecurityConfig.passwordEncoder()`, restart

---

### 3. 👁️ Observer Pattern

**Location:** `model/Course.java` and `model/Purchase.java`

**Purpose:** Automatically set timestamps when entities are persisted to database.

**Implementation:**
```java
@Entity
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    
    private String title;
    private double price;
    
    @CreationTimestamp  // Observer annotation
    @Column(nullable = false, updatable = false)
    private Instant createdAt;  // Auto-populated!
}
```

**How It Works:**
1. **Subject:** JPA EntityManager
2. **Observer:** `@CreationTimestamp` annotation processor
3. **Event:** Entity persist/insert operation
4. **Notification:** Annotation sets timestamp automatically

**Without Observer:**
```java
// Every service would need:
@Service
public class CourseService {
    public void createCourse(Course course) {
        course.setCreatedAt(Instant.now()); // Manual timestamp
        courseRepo.save(course);
    }
}
```

**With Observer:**
```java
// No timestamp code needed anywhere!
@Service
public class CourseService {
    public void createCourse(Course course) {
        courseRepo.save(course); // Timestamp auto-set!
    }
}
```

**Benefits:**
- Eliminates boilerplate in every service
- Consistent timestamps across all entities
- Timestamp set by database transaction time (accurate)
- DRY principle (Don't Repeat Yourself)

**Test:** Create course as admin, check console logs for auto-set timestamp

---

### 4. 🏭 Factory Method Pattern

**Location:** `DesignPatternsDemoApplication.java`

**Purpose:** Encapsulate complex object creation logic in factory methods.

**Implementation:**
```java
@SpringBootApplication
public class DesignPatternsDemoApplication {
    
    @Bean
    CommandLineRunner createAdmin(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("demo123"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN));
                userRepo.save(admin);
            }
        };
    }
    
    @Bean
    CommandLineRunner seedCourses(CourseRepository courseRepo) {
        return args -> {
            if (courseRepo.count() == 0) {
                courseRepo.save(new Course("Title", "Desc", 99.0, 10));
                // ... more courses
            }
        };
    }
}
```

**Without Factory Method:**
```java
// Scattered throughout application:
// In some random service:
if (!userExists("admin")) {
    User admin = new User();
    admin.setUsername("admin");
    // ... 10 more lines
}
// In another service:
if (courses.isEmpty()) {
    // ... duplicate creation logic
}
```

**With Factory Method:**
- All creation logic centralized in main application
- Automatic execution at startup
- Easy to modify (e.g., send welcome emails)
- Spring manages dependencies

**Benefits:**
- Single point of truth for object creation
- Automatic lifecycle management
- Easy to test in isolation
- Can add logging, validation, notifications

**Test:** Restart application, observe startup console logs

---

### 5. 📋 Template Method Pattern

**Location:** `service/CustomUserDetailsService.java`

**Purpose:** Customize one step in Spring Security's authentication algorithm.

**Implementation:**
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        // THIS IS THE ONLY METHOD WE IMPLEMENT!
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        
        var authorities = user.getRoles().stream()
            .map(Role::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            authorities
        );
    }
}
```

**Spring Security's Authentication Algorithm (Template):**
```
1. Extract username from HTTP request        ← Framework
2. Call loadUserByUsername(username)         ← WE IMPLEMENT THIS
3. Extract password from HTTP request        ← Framework
4. Compare passwords using PasswordEncoder   ← Framework
5. Check if account is locked/expired        ← Framework
6. Check authorities and roles               ← Framework
7. Create Authentication token               ← Framework
8. Store in SecurityContext                  ← Framework
9. Handle remember-me, session fixation      ← Framework
10. Redirect to success URL                  ← Framework
```

**We implement step 2. Framework handles steps 1, 3-10.**

**Without Template Method:**
```java
// We would need to implement ALL steps:
public Authentication authenticate(String username, String password) {
    // Extract request params
    // Validate format
    // Query database
    // Compare passwords
    // Check account status
    // Handle exceptions
    // Create token
    // Store in context
    // Handle session
    // ... 200+ lines of security-critical code
}
```

**Benefits:**
- Leverage battle-tested authentication framework
- Don't reinvent the wheel (security is hard!)
- Can swap user source (DB, LDAP, OAuth) easily
- Framework handles complex security concerns
- Reduces custom code from 200+ lines to 15 lines

**Test:** Login with any credentials, observe console logs

---

### 6. 🔷 Singleton Pattern

**Location:** All `@Controller`, `@Service`, `@Configuration` classes

**Purpose:** Spring manages beans as singletons for efficient resource usage.

**Implementation:**
```java
@Controller
public class HomeController {
    public HomeController() {
        System.out.println("🎯 [SINGLETON] HomeController instance created");
    }
    
    @GetMapping("/")
    public String home() {
        // Same instance handles ALL requests from ALL users
        return "home";
    }
}
```

**How Spring Manages Singletons:**
1. Creates ONE instance at application startup
2. Stores in Application Context (singleton registry)
3. Injects dependencies once
4. Reuses same instance for all requests
5. Thread-safe concurrent access

**Exception:** CartService is `@SessionScope`
```java
@Service
@SessionScope  // One instance PER USER SESSION
public class CartService {
    // Each user gets their own cart instance
}
```

**Without Singleton (if we created new instances):**
```java
@GetMapping("/")
public String home() {
    // ❌ BAD: Creating new instance per request
    HomeService service = new HomeService(dep1, dep2, dep3);
    // Memory leak! Thousands of instances!
}
```

**With Singleton (Spring default):**
```java
@Controller
public class HomeController {
    private final HomeService service; // Injected once
    
    public HomeController(HomeService service) {
        this.service = service; // Same instance reused
    }
}
```

**Benefits:**
- Memory efficient (one instance vs thousands)
- Thread-safe concurrent handling
- Automatic lifecycle management
- Easy dependency injection
- No manual getInstance() code needed

**Test:** Check startup logs (instance created once), make multiple requests (same instance logs)

---

## Pattern Interactions

Patterns work together in this application:

1. **Singleton + Facade:** Singleton CartController uses Facade CartService
2. **Singleton + Strategy:** Singleton AuthController uses Strategy PasswordEncoder
3. **Facade + Observer:** Facade methods save entities with Observer timestamps
4. **Factory + Strategy:** Factory methods use Strategy to encode passwords
5. **Template + Strategy:** Template Method uses Strategy for password comparison
6. **Observer + Factory:** Factory-created entities have Observer timestamps

---

## Testing Each Pattern

See `TESTING_GUIDE.md` for detailed testing instructions.

**Quick Test Commands:**
```bash
# Start application
./run.sh          # Linux/Mac
run.bat           # Windows

# Or manually
mvn clean install
mvn spring-boot:run

# Access application
http://localhost:8080

# Login credentials
admin / demo123
user / user123
```

---

## Console Log Legend

When running, watch for these logs:

- `🎯 [SINGLETON]` - Singleton creation/usage
- `🎭 [FACADE]` - Facade method calls
- `🔐 [STRATEGY]` - Strategy pattern usage
- `👁️ [OBSERVER]` - Observer pattern activation
- `🏭 [FACTORY METHOD]` - Factory execution
- `🔍 [TEMPLATE METHOD]` - Template customization

---

## Key Takeaways

1. **Facade** simplifies complex subsystems
2. **Strategy** allows algorithm swapping
3. **Observer** enables event-driven behavior
4. **Factory Method** centralizes object creation
5. **Template Method** customizes algorithm steps
6. **Singleton** ensures single instance

All patterns are **production-ready** and follow **Spring best practices**.

---

## Educational Value

This project demonstrates:
- ✅ Real-world pattern usage (not toy examples)
- ✅ Pattern interactions and combinations
- ✅ Spring Framework pattern implementations
- ✅ Proper separation of concerns
- ✅ Clean code and SOLID principles
- ✅ Extensive documentation and logging
- ✅ Fully runnable and testable

Perfect for learning, teaching, or interview preparation!
