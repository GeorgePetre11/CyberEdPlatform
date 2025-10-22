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

This project implements **five non-trivial design patterns** to ensure maintainability, scalability, and adherence to SOLID principles. Each pattern directly addresses specific project requirements and offers clear advantages over simpler alternatives.

### 1. Repository Pattern

**Implementation**: `CourseRepository.java`, `UserRepository.java`, `ForumPostRepository.java`, `ForumCommentRepository.java`, `ChallengeRepository.java`, `PurchaseRepository.java`

**Purpose**: Abstracts data access layer from business logic, providing a collection-like interface for domain objects.

**Project-Specific Problem Solved**:
The platform requires complex queries across multiple entities (courses with enrollments, forum posts with comments, user purchase history). Without Repository Pattern, every controller and service would contain repetitive database access code using JPA's EntityManager, creating tight coupling to the persistence mechanism.

**Why Not Simpler Alternatives?**
- **Direct EntityManager Usage**: Would require writing `entityManager.persist()`, `entityManager.find()`, `createQuery()` repeatedly in every service class. Repository Pattern provides these operations automatically through `save()`, `findById()`, `findAll()`.
- **Traditional DAO Pattern**: Requires implementing concrete classes with boilerplate CRUD code. Spring Data JPA repositories eliminate this through interface-based programming—a single interface declaration provides complete CRUD functionality.
- **Testability Advantage**: Repositories are easily mocked without database connections, crucial for testing forum moderation logic and course purchase workflows.

---

### 2. Model-View-Controller (MVC) Pattern

**Implementation**: Controllers (`CourseController.java`, `ForumController.java`), Models (`Course.java`, `User.java`), Views (Thymeleaf templates)

**Purpose**: Separates presentation, business logic, and data into distinct components.

**Project-Specific Problem Solved**:
The platform serves multiple user roles (admins managing courses, users browsing/purchasing, forum participants) with different UI needs. Without MVC, HTML generation, business rules, and data access would be intertwined in servlet code, making it impossible to change the UI without modifying business logic or add role-specific views.

**Why Not Simpler Alternatives?**
- **Page Controller Pattern**: Each URL would need its own controller class, leading to code duplication for authentication checks, cart management, and error handling across dozens of endpoints.
- **Transaction Script**: Would result in procedural code handling each request from start to finish, making it impossible to reuse business logic (e.g., course enrollment validation) across web interface and future REST API.
- **Parallel Development**: Frontend developers can iterate on Thymeleaf templates (forum UI, course catalog) while backend developers implement challenge scoring and purchase processing independently.

---

### 3. Dependency Injection (DI) Pattern

**Implementation**: All `@Service`, `@Controller`, `@Repository` classes; `SecurityConfig.java`; `CartAdvice.java`

**Purpose**: Inverts control of object creation, allowing Spring IoC container to manage dependencies and lifecycles.

**Project-Specific Problem Solved**:
The platform has complex dependencies: controllers need repositories, security configuration needs UserDetailsService, cart service needs course repository, forum controllers need authentication. Manual instantiation would create rigid coupling and prevent configuration changes (e.g., switching to caching repository implementations).

**Why Not Simpler Alternatives?**
- **Service Locator Pattern**: Hides dependencies—code calling `ServiceLocator.get(CourseRepository.class)` doesn't declare what it needs, making dependency tracking impossible and breaking compile-time safety.
- **Manual Factory Pattern**: Requires writing factory classes for every service and managing singleton lifecycles manually, leading to hundreds of lines of boilerplate and potential thread-safety bugs.
- **Constructor Injection Advantage**: Dependencies are explicit and immutable, enabling compile-time validation and easy unit testing with mocks.

---

### 4. Template Method Pattern

**Implementation**: `CustomUserDetailsService.java` implementing `UserDetailsService`

**Purpose**: Defines authentication algorithm skeleton while allowing custom user-loading implementation.

**Project-Specific Problem Solved**:
Spring Security handles authentication flow (credential validation, session creation, security context management), but user data storage is application-specific (database, LDAP, OAuth). The platform stores users in MariaDB with custom Role enum. Template Method allows implementing only the user-loading step while inheriting the complete authentication framework.

**Why Not Simpler Alternatives?**
- **Strategy Pattern**: Would allow swapping entire authentication algorithms, but we only need to customize user-loading—the authentication flow (validate password, create token, establish session) remains constant.
- **Direct Implementation**: Writing authentication from scratch would require handling password hashing, CSRF tokens, session fixation protection, remember-me functionality—hundreds of lines prone to security vulnerabilities.
- **Framework Integration**: Spring Security automatically calls `loadUserByUsername()` during login, password resets, and session validation.

---

### 5. Front Controller Pattern

**Implementation**: Spring Boot's `DispatcherServlet` (auto-configured), all `@Controller` classes, `SecurityConfig.java`

**Purpose**: Provides single entry point for all HTTP requests, enabling centralized request processing.

**Project-Specific Problem Solved**:
The platform has cross-cutting concerns affecting all endpoints: authentication (login required for cart/forum), authorization (admin-only course management), cart display in navigation, CSRF protection, exception handling. Without Front Controller, these would be duplicated across separate servlets for courses, forum, challenges, and admin panels.

**Why Not Simpler Alternatives?**
- **Traditional Servlet Mapping**: Each feature (courses, forum, challenges) would need separate servlets with duplicate authentication filters, error handlers, and logging code. Adding global features (e.g., audit logging) would require modifying every servlet.
- **Page Controller**: Individual controllers without centralized dispatch cannot share pre/post-processing logic. The `CartAdvice` class making `CartService` available to all views would be impossible.
- **Aspect-Oriented Features**: `@ControllerAdvice` enables global exception handling and model attributes executed before every controller method.

---
## SOLID Principles Adherence


- **Single Responsibility**: Each controller handles one domain (courses, forum, challenges); services encapsulate specific business logic; repositories manage data access for single entities
- **Open/Closed**: Repositories extend `JpaRepository` adding custom queries without modifying base functionality; security configuration extends `WebSecurityConfigurerAdapter`
- **Liskov Substitution**: `CustomUserDetailsService` can be replaced with any `UserDetailsService` implementation without affecting authentication flow
- **Interface Segregation**: Repositories expose only necessary methods (`findByUsername`, `existsByUsername`) rather than forcing clients to depend on unused operations
- **Dependency Inversion**: Controllers and services depend on repository interfaces (abstractions), not concrete JPA implementations

---