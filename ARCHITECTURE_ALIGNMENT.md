# Architecture Alignment with Original Project

## Overview

This document shows how the Milestone 4 microservices implementation aligns with the original CyberEd Platform from Milestones 2 & 3.

---

## Model Alignment

### Original Monolithic Models → Microservices Models

#### User Model
**Original Location:** `org.CyberEdPlatform.patterns.model.User`  
**Microservice Location:** `user-service/model/User.java`

**Key Features Preserved:**
- ✅ `Set<Role>` with `@ElementCollection` for role management
- ✅ BCrypt password encoding (Strategy Pattern)
- ✅ Username uniqueness constraint
- ✅ Same field structure (id, username, password, roles)

#### Course Model  
**Original Location:** `org.CyberEdPlatform.patterns.model.Course`  
**Microservice Location:** `course-service/model/Course.java`

**Key Features Preserved:**
- ✅ `@CreationTimestamp` annotation (Observer Pattern)
- ✅ Fields: id, title, description, price, quantity, createdAt
- ✅ Inventory management logic
- ✅ Same business rules

#### Purchase Model (formerly "Order" in microservice)
**Original Location:** `org.CyberEdPlatform.patterns.model.Purchase`  
**Microservice Location:** `order-service/model/Purchase.java`

**Key Features Preserved:**
- ✅ `@CreationTimestamp` with `purchasedAt` field (Observer Pattern)
- ✅ Relationship to User and Course (via IDs in microservices)
- ✅ Same purchase tracking logic

---

## Design Patterns Preserved Across Microservices

### From Original Project (Milestone 2 & 3)

The original monolithic application demonstrated 6 design patterns:

1. **Facade Pattern** → CartService simplifies cart operations
2. **Strategy Pattern** → BCryptPasswordEncoder for password hashing
3. **Observer Pattern** → @CreationTimestamp for automatic timestamps
4. **Factory Method Pattern** → @Bean methods create objects
5. **Template Method Pattern** → CustomUserDetailsService extends framework
6. **Singleton Pattern** → Spring-managed beans

### How They Appear in Microservices

#### 1. **Facade Pattern** ✅ Preserved
- **Original:** `CartService` in monolith
- **Microservices:** `CartService` in order-service (session-scoped cart management)
- **Plus:** `ServiceClient` acts as Facade for inter-service communication

#### 2. **Strategy Pattern** ✅ Preserved  
- **Original:** `BCryptPasswordEncoder` configured in SecurityConfig
- **Microservices:** Same BCryptPasswordEncoder in user-service
- **Location:** `UserService.registerUser()` uses BCrypt

#### 3. **Observer Pattern** ✅ Preserved
- **Original:** `@CreationTimestamp` on Course.createdAt and Purchase.purchasedAt
- **Microservices:**  
  - `Course.createdAt` with `@CreationTimestamp`
  - `Purchase.purchasedAt` with `@CreationTimestamp`

#### 4. **Factory Method Pattern** ✅ Preserved
- **Original:** `@Bean` methods in configuration classes
- **Microservices:** 
  - `RestTemplate` bean in OrderServiceApplication
  - `BCryptPasswordEncoder` bean in user-service configuration
  - `DataInitializer` beans in all services

#### 5. **Template Method Pattern** ✅ Preserved (Adapted)
- **Original:** `CustomUserDetailsService` extends `UserDetailsService`
- **Microservices:** RESTful service interfaces define template for implementations
- **Plus:** Service layer abstractions (UserService, CourseService, OrderService)

#### 6. **Singleton Pattern** ✅ Preserved
- **Original:** All Spring `@Service`, `@Controller`, `@Repository` beans
- **Microservices:** Same singleton pattern with `@Service`, `@RestController`, `@Repository`

---

## Service Decomposition Strategy

### Original Monolithic Structure
```
code/
├── model/
│   ├── User.java
│   ├── Course.java
│   └── Purchase.java
├── repository/
│   ├── UserRepository.java
│   ├── CourseRepository.java
│   └── PurchaseRepository.java
├── service/
│   ├── CartService.java
│   └── CustomUserDetailsService.java
└── controller/
    ├── AuthController.java
    ├── CourseController.java
    ├── CartController.java
    └── AdminController.java
```

### Microservices Decomposition
```
user-service/
├── model/User.java (from monolith)
├── repository/UserRepository.java (from monolith)
└── controller/UserController.java (REST API for auth)

course-service/
├── model/Course.java (from monolith)
├── repository/CourseRepository.java (from monolith)
└── controller/CourseController.java (REST API for catalog)

order-service/
├── model/Purchase.java (from monolith Purchase)
├── repository/OrderRepository.java (from Purchase logic)
├── service/CartService.java (FACADE PATTERN from monolith)
└── client/ServiceClient.java (NEW: inter-service communication)
```

---

## Key Differences & Enhancements

### What Changed in Microservices

#### 1. **Database per Service** (NEW)
- **Original:** Single H2 database for all models
- **Microservices:** Separate H2 database per service
  - `user_db` for User Service
  - `course_db` for Course Service
  - `order_db` for Order Service

#### 2. **Inter-Service Communication** (NEW)
- **Original:** Direct Java method calls between services
- **Microservices:** REST API calls via `RestTemplate`
- **Example:** Order Service calls User Service and Course Service

#### 3. **RESTful APIs** (ENHANCED)
- **Original:** MVC with Thymeleaf templates (HTML responses)
- **Microservices:** Pure REST APIs with JSON responses
- **Better for:** Service-to-service communication and API consumers

#### 4. **Docker Containerization** (NEW)
- **Original:** Run as single JAR (`java -jar`)
- **Microservices:** Each service in separate Docker container
- **Orchestration:** docker-compose manages all services

### What Stayed the Same

✅ **Core Business Logic** - Purchase flow, user registration, course management  
✅ **Design Patterns** - All 6 patterns preserved or adapted  
✅ **Data Models** - Same entities with same fields  
✅ **Spring Boot Framework** - Same version and dependencies  
✅ **H2 Database** - In-memory database (now per-service)

---

## Pattern Mapping Table

| Pattern | Original Location | Microservice Location | Status |
|---------|------------------|---------------------|--------|
| Facade | CartService | order-service/CartService + ServiceClient | ✅ Enhanced |
| Strategy | SecurityConfig BCrypt | user-service/UserService BCrypt | ✅ Preserved |
| Observer | @CreationTimestamp | Course & Purchase models | ✅ Preserved |
| Factory Method | @Bean methods | Application classes | ✅ Preserved |
| Template Method | CustomUserDetailsService | Service abstractions | ✅ Adapted |
| Singleton | Spring beans | Spring beans | ✅ Preserved |

---

## Testing Alignment

### Original Testing Flow
1. Start monolith: `mvn spring-boot:run`
2. Visit `http://localhost:8080`
3. Register user → Browse courses → Add to cart → Checkout

### Microservices Testing Flow
1. Start all services: `docker-compose up --build`
2. Use Postman collection or curl commands
3. Register user (User Service) → Browse courses (Course Service) → Checkout (Order Service)
4. **Same business logic, different architecture**

---

## Conclusion

The microservices implementation successfully:

✅ **Preserves** all 6 design patterns from Milestone 2 & 3  
✅ **Maintains** core business logic and data models  
✅ **Decomposes** monolith into 3 independent services  
✅ **Adds** inter-service communication via REST  
✅ **Introduces** Docker containerization  
✅ **Enhances** scalability and maintainability  

**This is a proper evolution from monolith to microservices, not a rewrite.**
