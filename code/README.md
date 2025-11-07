# CyberEdPlatform Project

Part of the CyberEdPlatform project, where the only code present is to present the 6 design patterns used.

## Design Patterns Demonstrated

1. **Facade Pattern** - `CartService` - Simplifies shopping cart operations
2. **Strategy Pattern** - `PasswordEncoder` - Interchangeable password hashing algorithms
3. **Observer Pattern** - `@CreationTimestamp` - Automatic timestamp population
4. **Factory Method Pattern** - `@Bean` methods - Object creation at startup
5. **Template Method Pattern** - `CustomUserDetailsService` - Custom authentication
6. **Singleton Pattern** - Spring-managed beans - Single instance per application

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running the Application

```bash
cd design-patterns-demo
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Credentials
- **Username**: `admin`
- **Password**: `demo123`

## Testing the Patterns

### 1. Facade Pattern (CartService)
- Login and navigate to `/courses`
- Click "Add to Cart" on any course
- View cart at `/cart`
- CartService hides complexity of cart operations

### 2. Strategy Pattern (PasswordEncoder)
- Register a new user at `/register`
- Password is encrypted using BCrypt strategy
- Can be swapped with Argon2 or SCrypt without code changes

### 3. Observer Pattern (@CreationTimestamp)
- Create a course (admin only) at `/admin/courses/create`
- Notice `createdAt` timestamp is automatically set
- Same for purchases when checking out

### 4. Factory Method Pattern (@Bean methods)
- Check application startup logs
- See: "Created default admin", "Seeded 3 courses"
- Factory methods in `DesignPatternsDemoApplication.java`

### 5. Template Method Pattern (CustomUserDetailsService)
- Login at `/login`
- `loadUserByUsername()` is called (our custom implementation)
- Rest of authentication handled by Spring Security framework

### 6. Singleton Pattern (Spring Beans)
- All controllers are singleton beans
- Exception: CartService is session-scoped (one per user)
- Check by adding debug logs in controllers

## Project Structure

```
design-patterns-demo/
├── src/main/java/org/example/patterns/
│   ├── DesignPatternsDemoApplication.java  (Factory Method)
│   ├── model/
│   │   ├── User.java
│   │   ├── Course.java                      (Observer Pattern)
│   │   └── Purchase.java                    (Observer Pattern)
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── CourseRepository.java
│   │   └── PurchaseRepository.java
│   ├── service/
│   │   ├── CartService.java                 (Facade Pattern)
│   │   └── CustomUserDetailsService.java    (Template Method)
│   ├── config/
│   │   ├── SecurityConfig.java              (Strategy Pattern)
│   │   └── CartAdvice.java
│   └── controller/
│       ├── HomeController.java              (Singleton)
│       ├── AuthController.java              (Singleton)
│       ├── CourseController.java            (Singleton)
│       ├── CartController.java              (Singleton)
│       └── AdminController.java             (Singleton)
└── src/main/resources/
    ├── templates/
    │   ├── home.html
    │   ├── login.html
    │   ├── register.html
    │   ├── courses.html
    │   ├── cart.html
    │   └── admin-courses.html
    └── application.properties
```

## Key Features

- Simplified version focusing only on design patterns
- Uses H2 in-memory database (no external DB needed)
- All 6 patterns fully functional and demonstrable
- Minimal UI for testing each pattern
- Automatic data seeding at startup
- Complete authentication and authorization


