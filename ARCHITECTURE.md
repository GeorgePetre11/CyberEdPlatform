# CyberEd Platform - Microservices Architecture

**Milestone 4: Microservices Implementation**

**Team Members:**
- Petre George-Alexandru, group 1241EB
- Ionescu Rares-Andrei, group 1241EB
- Leonte Robert, group 1241EB

**Date:** December 4, 2025

---

## Architecture Overview

The CyberEd Platform has been decomposed into **3 independent microservices**, each with its own database and responsibilities.

### Service Decomposition

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  User Service   │     │ Course Service  │     │ Order Service   │
│   Port 8081     │     │   Port 8082     │     │   Port 8083     │
│                 │     │                 │     │                 │
│ - Registration  │     │ - Course CRUD   │     │ - Cart Mgmt     │
│ - Authentication│     │ - Course List   │     │ - Purchases     │
│ - User Profile  │     │ - Inventory     │     │ - Order History │
│                 │     │                 │     │                 │
│  [User DB]      │     │  [Course DB]    │     │  [Order DB]     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                    │         │
                                                    │  REST   │
                                                    ▼         ▼
                                                 User API  Course API
```

---

## Service Details

### 1. User Service (Port 8081)

**Responsibilities:**
- User registration and authentication
- User profile management
- Role-based access control (USER, ADMIN)

**Database:** H2 (user_db)

**REST APIs:**
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Authenticate user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users` - List all users (admin)
- `DELETE /api/users/{id}` - Delete user (admin)

**Models:**
- User (id, username, password, roles)
- Role (ROLE_USER, ROLE_ADMIN)

---

### 2. Course Service (Port 8082)

**Responsibilities:**
- Course catalog management
- Course CRUD operations
- Inventory tracking

**Database:** H2 (course_db)

**REST APIs:**
- `GET /api/courses` - List all courses
- `GET /api/courses/{id}` - Get course by ID
- `POST /api/courses` - Create course (admin)
- `PUT /api/courses/{id}` - Update course (admin)
- `DELETE /api/courses/{id}` - Delete course (admin)
- `PUT /api/courses/{id}/inventory` - Update inventory (internal)

**Models:**
- Course (id, title, description, price, quantity, createdAt)

---

### 3. Order Service (Port 8083)

**Responsibilities:**
- Shopping cart management
- Purchase processing
- Order history

**Database:** H2 (order_db)

**REST APIs:**
- `POST /api/orders/cart/add` - Add course to cart
- `GET /api/orders/cart/{userId}` - View cart
- `POST /api/orders/checkout` - Process purchase
- `GET /api/orders/user/{userId}` - Get order history
- `GET /api/orders/{id}` - Get order details

**Models:**
- Order (id, userId, courseId, status, totalPrice, createdAt)
- CartItem (userId, courseId, courseName, price)

**Inter-Service Communication:**
- Calls User Service to validate user exists
- Calls Course Service to get course details and update inventory

---

## Inter-Service Communication

### Communication Pattern: REST API

**Order Service → User Service**
```
GET http://user-service:8081/api/users/{userId}
Purpose: Validate user exists before purchase
```

**Order Service → Course Service**
```
GET http://course-service:8082/api/courses/{courseId}
Purpose: Get course details (price, availability)

PUT http://course-service:8082/api/courses/{courseId}/inventory
Body: { "quantity": -1 }
Purpose: Decrease inventory after purchase
```

### Error Handling
- Service unavailable: Return 503 Service Unavailable
- Invalid data from service: Return 400 Bad Request
- Timeout: 5 second timeout with fallback

---

## Technology Stack

**Framework:** Spring Boot 3.4.5
**Language:** Java 17
**Database:** H2 (in-memory for development)
**Build Tool:** Maven
**Containerization:** Docker & Docker Compose
**API Testing:** Postman

---

## Design Patterns Used

1. **Microservices Pattern** - Service decomposition by business capability
2. **Database per Service** - Each service has its own database
3. **API Gateway Pattern** - Single entry point (optional for future)
4. **RESTful API** - Standard HTTP/JSON communication
5. **Repository Pattern** - Data access abstraction
6. **DTO Pattern** - Data transfer objects for API responses

---

## Scalability

This architecture is designed to easily scale:

**Add 4th Service (Challenge Service):**
- Copy service structure
- Add to docker-compose.yml
- No changes to existing services

**Add 5th Service (Forum Service):**
- Same pattern, completely independent
- Takes ~30 minutes to add

---

## Next Steps

1. ✅ Define architecture
2. 🔄 Implement User Service
3. ⏳ Implement Course Service
4. ⏳ Implement Order Service
5. ⏳ Create Docker configuration
6. ⏳ Build Postman collection
7. ⏳ Write README with instructions
