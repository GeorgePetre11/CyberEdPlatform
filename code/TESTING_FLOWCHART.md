# Pattern Testing Flowchart

## Application Flow with Pattern Demonstrations

```
┌─────────────────────────────────────────────────────────────────┐
│                    APPLICATION STARTUP                          │
│  🏭 FACTORY METHOD PATTERN - @Bean methods execute              │
├─────────────────────────────────────────────────────────────────┤
│  ✓ createAdmin() → Creates admin user                          │
│  ✓ seedCourses() → Creates 3 initial courses                   │
│  ✓ createDemoUser() → Creates regular user                     │
│                                                                 │
│  👁️ OBSERVER PATTERN - Courses get auto-set timestamps          │
│  🔐 STRATEGY PATTERN - Passwords encoded with BCrypt            │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    CONTROLLER CREATION                          │
│  🔷 SINGLETON PATTERN - Spring creates controller instances     │
├─────────────────────────────────────────────────────────────────┤
│  ✓ HomeController created (once)                               │
│  ✓ AuthController created (once)                               │
│  ✓ CourseController created (once)                             │
│  ✓ CartController created (once)                               │
│  ✓ AdminController created (once)                              │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│              USER VISITS http://localhost:8080                  │
│  🔷 SINGLETON - Same HomeController instance handles request    │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                  USER CLICKS "REGISTER"                         │
│  🔐 STRATEGY PATTERN in action                                  │
├─────────────────────────────────────────────────────────────────┤
│  1. User enters username & password                            │
│  2. AuthController.register() called                           │
│  3. passwordEncoder.encode(password) ← Strategy used           │
│  4. BCryptPasswordEncoder encrypts password                    │
│  5. User saved to database                                     │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    USER CLICKS "LOGIN"                          │
│  📋 TEMPLATE METHOD PATTERN in action                           │
├─────────────────────────────────────────────────────────────────┤
│  1. User enters credentials                                    │
│  2. Spring Security calls loadUserByUsername() ← We implement  │
│  3. CustomUserDetailsService loads user from DB                │
│  4. Framework compares passwords (Strategy Pattern)            │
│  5. Framework creates authentication token                     │
│  6. Framework stores in SecurityContext                        │
│  7. User logged in! (Framework handled steps 4-7)              │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                  USER VIEWS COURSE LIST                         │
│  👁️ OBSERVER PATTERN visible                                    │
├─────────────────────────────────────────────────────────────────┤
│  ✓ Courses display with "Created At" timestamps               │
│  ✓ These were auto-set by @CreationTimestamp                  │
│  ✓ No manual timestamp code was needed                        │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                USER CLICKS "ADD TO CART"                        │
│  🎭 FACADE PATTERN in action                                    │
├─────────────────────────────────────────────────────────────────┤
│  1. CartController.addToCart(id) called                        │
│  2. cartService.add(id) ← Simple facade method                 │
│  3. Internally: Map manipulation, no controller logic          │
│  4. User sees cart count updated                               │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    USER VIEWS CART                              │
│  🎭 FACADE PATTERN - Multiple facade methods                    │
├─────────────────────────────────────────────────────────────────┤
│  ✓ cartService.getItems() - Transforms Map to Course objects  │
│  ✓ cartService.getTotalItems() - Calculates total quantity    │
│  ✓ cartService.getTotalPrice() - Calculates total price       │
│  ✓ cartService.isEmpty() - Checks if cart has items           │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                  USER CLICKS "CHECKOUT"                         │
│  🎭 FACADE + 👁️ OBSERVER patterns together                      │
├─────────────────────────────────────────────────────────────────┤
│  1. CartController.checkout() called                           │
│  2. cartService.getItems() ← Facade retrieves courses          │
│  3. Create Purchase entities for each item                     │
│  4. Save purchases → @CreationTimestamp sets timestamps! ← Observer │
│  5. cartService.clear() ← Facade empties cart                  │
│  6. Redirect to home                                           │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│            ADMIN CLICKS "CREATE NEW COURSE"                     │
│  👁️ OBSERVER PATTERN in action                                  │
├─────────────────────────────────────────────────────────────────┤
│  1. Admin enters course details                                │
│  2. AdminController.createCourse() called                      │
│  3. courseRepository.save(course)                              │
│  4. @CreationTimestamp automatically sets createdAt! ← Observer │
│  5. Course saved with timestamp                                │
│  6. Console shows: "CreatedAt: 2025-11-06T10:30:45Z (auto!)"  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                   PATTERN SUMMARY TABLE                         │
├─────────────────────────────────────────────────────────────────┤
│  Pattern          │ When Triggered        │ Observable Result  │
├───────────────────┼──────────────────────┼────────────────────┤
│ 🏭 Factory Method │ App startup          │ Startup logs       │
│ 🔷 Singleton      │ App startup + reqs   │ "Instance created" │
│ 🔐 Strategy       │ Register/Login       │ Password encrypted │
│ 📋 Template Meth. │ Login                │ "Loading user"     │
│ 👁️ Observer       │ Save entity          │ Auto timestamp     │
│ 🎭 Facade         │ Cart operations      │ Simple method call │
└─────────────────────────────────────────────────────────────────┘

═══════════════════════════════════════════════════════════════════
                    COMPLETE TESTING PATHS
═══════════════════════════════════════════════════════════════════

PATH 1: Full Shopping Flow
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Startup → Login (Template) → View Courses (Observer) → 
Add to Cart (Facade) → View Cart (Facade) → Checkout (Facade+Observer)

Patterns Demonstrated: 5/6 (all except Factory - seen at startup)

PATH 2: Admin Flow
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Startup (Factory) → Login as admin (Template) → Admin Panel (Singleton) → 
Create Course (Observer) → View timestamp

Patterns Demonstrated: 4/6 (Factory, Template, Observer, Singleton)

PATH 3: Registration Flow
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Home → Register (Strategy) → Login (Template+Strategy) → View Courses

Patterns Demonstrated: 2/6 (Strategy, Template)

PATH 4: Pattern Modification
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
1. Open SecurityConfig.java
2. Change: return new Argon2PasswordEncoder(); (Strategy switch!)
3. Restart app (see Factory logs again)
4. Register new user (now uses Argon2 instead of BCrypt)
5. Demonstrates Strategy Pattern flexibility!

═══════════════════════════════════════════════════════════════════
```

## Console Log Examples

### Startup (Factory Method + Singleton)
```
🏭 [FACTORY METHOD] Creating admin user...
✅ [FACTORY METHOD] Admin user created successfully!
🏭 [FACTORY METHOD] Seeding courses...
✅ [FACTORY METHOD] Seeded 3 courses successfully!
🎯 [SINGLETON] HomeController instance created
🎯 [SINGLETON] AuthController instance created
```

### Login (Template Method + Strategy)
```
🔍 [TEMPLATE METHOD] Loading user: admin
✅ [TEMPLATE METHOD] User loaded with roles: [ROLE_ADMIN]
```

### Cart Operations (Facade)
```
📍 [SINGLETON] CartController adding to cart
🎭 [FACADE] Using CartService.add() - simple interface!
🎭 [FACADE] Using CartService.getItems() - hides complexity!
🎭 [FACADE] Using CartService.getTotalPrice()
```

### Checkout (Facade + Observer)
```
🎭 [FACADE] Using CartService.getItems() and getTotalPrice()
👁️ [OBSERVER] Purchase saved - @CreationTimestamp auto-set!
🎭 [FACADE] Using CartService.clear() to empty cart
```

### Course Creation (Observer)
```
📍 [SINGLETON] AdminController creating course
👁️ [OBSERVER] Course will be saved with auto-set createdAt timestamp!
✅ Course created: Advanced Pentesting
   CreatedAt: 2025-11-06T15:45:30.123Z (auto-set by Observer!)
```

---

**Pro Tip:** Keep the console/terminal visible while using the app to see patterns in real-time!
