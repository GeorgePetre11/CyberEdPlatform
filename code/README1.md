# CyberEdPlatform

## Team Members
- Petre George-Alexandru, group 1241EB
- Ionescu Rares-Andrei, group 1241EB
- Leonte Robert, group 1241EB

## Project Description

**CyberEdPlatform** is a comprehensive web-based educational system designed to provide structured learning experiences in cybersecurity. This platform serves as an all-in-one solution for learners to enhance their cybersecurity knowledge through interactive courses, hands-on challenges, and community engagement.

### Core Features and Implementation Requirements

**1. Course Management System**
   - Browse and purchase cybersecurity courses (Web Application Pentesting, Secure Coding, Network Security)
   - Admin CRUD operations for course content with inventory tracking
   - Detailed course descriptions, pricing, and enrollment management

**2. Interactive Challenge System**
   - Hands-on cybersecurity challenges with multiple difficulty levels (Beginner, Advanced)
   - Challenge completion tracking, scoring system, and user progress monitoring
   - Real-world penetration testing and security analysis scenarios

**3. E-Commerce & Shopping Cart**
   - Session-based shopping cart for course purchases
   - Secure checkout process with purchase history tracking
   - Integration with user authentication for personalized cart management

**4. Community Forum**
   - Discussion board for knowledge sharing with post creation and commenting
   - User authentication for content ownership and moderation capabilities
   - Admin moderation tools for content management

**5. User Management & Security**
   - Secure registration and authentication with BCrypt password encryption
   - Role-based access control (RBAC) for Admin and User roles
   - Session management for personalized user experiences

**6. Administrative Dashboard**
   - Comprehensive user management (create, edit, delete users)
   - Course content and inventory management
   - Platform statistics and monitoring tools

### Technical Stack

- **Backend**: Spring Boot 3.4.5 (Java 17), Spring Security 6, Spring Data JPA with Hibernate
- **Frontend**: Thymeleaf templating with responsive CSS
- **Database**: MariaDB 11
- **Containerization**: Docker and Docker Compose
- **Build Tool**: Maven

---

## Design Patterns Implementation

This project implements **six design patterns** to ensure maintainability, scalability, and adherence to software engineering best practices.

### 1. Facade Pattern 

**Implementation**: `CartService.java`

**Purpose**: Provides a simplified interface for cart operations, hiding the complexity of working with repositories and data transformations.

**Key Methods**:
- `getItems()` - Returns course-to-quantity mapping
- `add(Long courseId)` - Adds course to cart
- `remove(Long courseId)` - Removes course from cart
- `getTotalPrice()` - Calculates total cart value
- `clear()` - Empties the cart

**Why This Pattern?**
Without the Facade pattern, controllers would need to directly interact with repositories, handle Map operations, perform stream transformations, and calculate totals. The Facade simplifies these operations into clean, intuitive methods that hide the underlying complexity.

**Benefits**:
- Controllers use simple methods like `cart.add(id)` instead of complex repository and map operations
- Easy to modify implementation (e.g., add Redis caching) without affecting client code
- Improves testability by mocking a single service instead of multiple subsystems

---

### 2. Strategy Pattern 

**Implementation**: `SecurityConfig.java` - `passwordEncoder()` method

**Purpose**: Uses `PasswordEncoder` interface with BCrypt strategy for password hashing. The encoding algorithm can be easily swapped without modifying client code.

**Current Strategy**: `BCryptPasswordEncoder`

**Alternative Strategies Available**:
- `Argon2PasswordEncoder` - Memory-hard algorithm resistant to GPU attacks
- `SCryptPasswordEncoder` - CPU and memory intensive
- `Pbkdf2PasswordEncoder` - PBKDF2 with HMAC-SHA algorithm

**Why This Pattern?**
Password hashing requirements evolve (e.g., transitioning from BCrypt to Argon2). Strategy pattern allows changing the hashing algorithm by modifying a single line of code while all authentication code using `PasswordEncoder` interface remains unchanged.

**Benefits**:
- Security upgrades don't require code changes throughout the application
- Can support multiple encoders during migration periods
- Easy to test with mock/no-op encoders in unit tests

---

### 3. Observer Pattern 

**Implementation**: `Purchase.java` and `Course.java` with `@CreationTimestamp` annotation

**Purpose**: Entities observe their own lifecycle events and automatically set timestamps when persisted to the database.

**Pattern Elements**:
- **Subject**: JPA/Hibernate EntityManager
- **Observer**: `@CreationTimestamp` annotation processor
- **Event**: Entity persist/insert operation
- **Notification**: Automatic timestamp population

**Why This Pattern?**
Without Observer pattern, every service creating purchases or courses would need manual timestamp code: `purchase.setPurchasedAt(Instant.now())`. This is error-prone and creates duplication. Observer pattern ensures timestamps are set automatically and consistently.

**Benefits**:
- Eliminates boilerplate timestamp code in services
- Guarantees consistent timestamp behavior across all entity operations
- Timestamps are set by database transaction time, not application time (more accurate)

---

### 4. Factory Method Pattern 

**Implementation**: `CybersecurityPlatformApplication.java` - `@Bean` methods

**Factory Methods**:
- `createAdmin()` - Creates admin user with predefined roles and credentials
- `seedCourses()` - Creates initial course catalog
- `seedChallenges()` - Creates cybersecurity challenge instances

**Purpose**: Encapsulates object creation logic with specific initialization requirements, ensuring consistent object configuration.

**Why This Pattern?**
Creating an admin user requires: checking existence, setting username, encoding password, assigning ROLE_ADMIN, and persisting. Without Factory Method, this logic would be scattered across multiple classes or duplicated. The pattern centralizes creation logic and runs it automatically at startup.

**Benefits**:
- Single point of truth for object creation logic
- Easy to modify initialization (e.g., add email notifications for new admins)
- Testable in isolation from application startup
- Spring ensures factories run in correct order with proper dependency injection

---

### 5. Template Method Pattern 

**Implementation**: `CustomUserDetailsService.java` implementing `UserDetailsService`

**Purpose**: Defines the algorithm skeleton for loading user details during authentication, while allowing customization of specific steps.

**Template Algorithm** (provided by Spring Security):
1. Extract username from login request
2. **Call `loadUserByUsername(username)`** ← Custom implementation
3. Compare passwords
4. Check authorities/roles
5. Create authentication token
6. Store in security context

**Our Custom Implementation**:
- Load user from database via `UserRepository`
- Convert custom `Role` enum to Spring Security authorities
- Return `UserDetails` object

**Why This Pattern?**
Authentication flow is complex (password validation, session management, CSRF protection, remember-me). Template Method lets us customize only user-loading while inheriting the entire secure authentication framework. Writing this from scratch would require hundreds of lines and risk security vulnerabilities.

**Benefits**:
- Leverage Spring Security's battle-tested authentication without reimplementing it
- Can extend for different user sources (LDAP, OAuth2) by implementing same template
- Framework handles security concerns automatically

---

### 6. Singleton Pattern  (Implicit via Spring)

**Implementation**: All `@Service`, `@Configuration`, `@Controller` classes

**Singleton Beans**:
- `CustomUserDetailsService` - One instance handles all authentication requests
- `SecurityConfig` - Single configuration instance
- All controllers - Same instance handles all HTTP requests (thread-safe)

**Exception**: `CartService` is `@SessionScope` (one instance per user session, not a singleton)

**Purpose**: Spring IoC Container manages beans as singletons by default, ensuring single instances throughout application lifecycle.

**Why This Pattern?**
Creating new service instances for every request would waste memory and lose stateful data. Singletons ensure efficient resource usage and consistent state. Spring provides this automatically without requiring explicit singleton implementation (no private constructors or `getInstance()` methods).

**Benefits**:
- Memory efficient - one instance serves all requests
- Thread-safe concurrent access managed by Spring
- Easy dependency injection - Spring manages instance lifecycle
- Simplifies configuration - no manual instance management

---
## SOLID Principles Adherence


- **Single Responsibility**: Each controller handles one domain (courses, forum, challenges); services encapsulate specific business logic; repositories manage data access for single entities
- **Open/Closed**: Repositories extend `JpaRepository` adding custom queries without modifying base functionality; security configuration extends `WebSecurityConfigurerAdapter`
- **Liskov Substitution**: `CustomUserDetailsService` can be replaced with any `UserDetailsService` implementation without affecting authentication flow
- **Interface Segregation**: Repositories expose only necessary methods (`findByUsername`, `existsByUsername`) rather than forcing clients to depend on unused operations
- **Dependency Inversion**: Controllers and services depend on repository interfaces (abstractions), not concrete JPA implementations

---