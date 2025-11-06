# Design Patterns Testing Guide

## How to Test Each Pattern

### 1. 🎭 **Facade Pattern** - CartService

**What to Test:**
- Adding items to cart
- Viewing cart
- Calculating totals
- Checkout process

**Steps:**
1. Login as any user (admin/demo123 or user/user123)
2. Go to **Courses** page
3. Click "Add to Cart" on any course
4. Go to **Cart** page
5. **Observe in console:**
   ```
   📍 [SINGLETON] CartController adding to cart
   🎭 [FACADE] Using CartService.add() - simple interface!
   ```
6. View cart to see:
   - `cartService.getItems()` - transforms Map to Course objects
   - `cartService.getTotalItems()` - calculates total
   - `cartService.getTotalPrice()` - sums prices
7. Click Checkout

**What Makes It Facade:**
- Controller only calls simple methods like `add()`, `getItems()`
- CartService hides complexity of Map operations, repository lookups, calculations
- Easy to modify internal implementation without affecting controllers

---

### 2. 🔄 **Strategy Pattern** - PasswordEncoder

**What to Test:**
- Password encoding during registration
- Password verification during login

**Steps:**
1. Click **Register**
2. Create a new user (e.g., testuser/test123)
3. **Observe in console:**
   ```
   🔐 [STRATEGY] Encoding password with current strategy
   🔐 [STRATEGY PATTERN] Creating BCryptPasswordEncoder
   ```
4. Login with the new credentials
5. Password is verified using the same strategy

**How to Switch Strategy:**
1. Open `SecurityConfig.java`
2. In `passwordEncoder()` method, uncomment a different strategy:
   ```java
   // return new BCryptPasswordEncoder();        // Current
   return new Argon2PasswordEncoder();           // Switch to this
   // return new SCryptPasswordEncoder();        // Or this
   ```
3. Restart application
4. All existing passwords still work (framework handles encoding prefix)

**What Makes It Strategy:**
- `PasswordEncoder` is the strategy interface
- `BCryptPasswordEncoder` is concrete strategy
- Can swap strategies without changing authentication code
- Client code uses interface, not concrete class

---

### 3. 👁️ **Observer Pattern** - @CreationTimestamp

**What to Test:**
- Automatic timestamp on Course creation
- Automatic timestamp on Purchase creation

**Steps for Course:**
1. Login as admin (admin/demo123)
2. Go to **Admin** → **Manage Courses**
3. Create a new course
4. **Observe in console:**
   ```
   📍 [SINGLETON] AdminController creating course
   👁️ [OBSERVER] Course will be saved with auto-set createdAt timestamp!
   ✅ Course created: Your Course Title
      CreatedAt: 2025-11-06T10:30:45.123Z (auto-set by Observer!)
   ```
5. Notice the "Created At" column shows timestamp automatically set

**Steps for Purchase:**
1. Add course to cart
2. Checkout
3. **Observe in console:**
   ```
   👁️ [OBSERVER] Purchase saved - @CreationTimestamp auto-set!
   ```

**What Makes It Observer:**
- `@CreationTimestamp` observes JPA entity lifecycle
- When entity is persisted, JPA notifies the annotation
- Timestamp automatically populated without manual code
- No `setCreatedAt(Instant.now())` calls needed anywhere

---

### 4. 🏭 **Factory Method Pattern** - @Bean Methods

**What to Test:**
- Admin user creation at startup
- Course seeding at startup
- Demo user creation at startup

**Steps:**
1. Stop the application if running
2. Delete H2 database file (if exists): `rm *.db`
3. Start application using `./run.sh` or `run.bat`
4. **Observe startup console logs:**
   ```
   ╔════════════════════════════════════════════════════════════╗
   ║       Design Patterns Demo Application Starting...        ║
   ╚════════════════════════════════════════════════════════════╝
   
   🏭 [FACTORY METHOD] Creating admin user...
   ✅ [FACTORY METHOD] Admin user created successfully!
      Username: admin
      Password: demo123
   
   🏭 [FACTORY METHOD] Seeding courses...
   ✅ [FACTORY METHOD] Seeded 3 courses successfully!
      🎓 Notice: @CreationTimestamp (Observer Pattern) auto-set timestamps!
   
   🏭 [FACTORY METHOD] Creating demo user...
   ✅ [FACTORY METHOD] Demo user created successfully!
      Username: user
      Password: user123
   
   ╔════════════════════════════════════════════════════════════╗
   ║            All Design Patterns Initialized!               ║
   ╠════════════════════════════════════════════════════════════╣
   ║ ✓ Facade Pattern       - CartService                      ║
   ║ ✓ Strategy Pattern     - PasswordEncoder (BCrypt)         ║
   ║ ✓ Observer Pattern     - @CreationTimestamp on entities   ║
   ║ ✓ Factory Method       - @Bean methods above              ║
   ║ ✓ Template Method      - CustomUserDetailsService         ║
   ║ ✓ Singleton Pattern    - All Spring-managed beans         ║
   ╚════════════════════════════════════════════════════════════╝
   ```

**What Makes It Factory Method:**
- `@Bean` methods encapsulate complex object creation
- Factory methods run automatically at startup
- Centralizes initialization logic
- Easy to modify (e.g., create more users, send emails)

---

### 5. 📋 **Template Method Pattern** - CustomUserDetailsService

**What to Test:**
- Authentication flow during login

**Steps:**
1. Logout if logged in
2. Go to **Login** page
3. Enter credentials: admin / demo123
4. **Observe in console:**
   ```
   🔍 [TEMPLATE METHOD] Loading user: admin
   ✅ [TEMPLATE METHOD] User loaded with roles: [ROLE_ADMIN, ROLE_USER]
   ```
5. Login succeeds without seeing all authentication steps

**What Makes It Template Method:**
- Spring Security defines authentication algorithm (template):
  1. Extract username ← Framework
  2. **Call loadUserByUsername()** ← We implement THIS ONLY
  3. Compare passwords ← Framework
  4. Check authorities ← Framework
  5. Create auth token ← Framework
  6. Store in context ← Framework
- We customize ONE step (step 2)
- Framework handles all other steps with battle-tested security

**To See Full Authentication:**
- Check `application.properties`: `logging.level.org.springframework.security=DEBUG`
- Restart and login to see complete authentication flow

---

### 6. 🔷 **Singleton Pattern** - Spring-Managed Beans

**What to Test:**
- Controllers are singletons (one instance per application)
- CartService is session-scoped (one instance per user session)

**Steps:**
1. Start application
2. **Observe startup console logs:**
   ```
   🎯 [SINGLETON] HomeController instance created
   🎯 [SINGLETON] AuthController instance created
   🎯 [SINGLETON] CourseController instance created
   🎯 [SINGLETON] CartController instance created
      ↳ Injected CartService (FACADE) - Session-scoped!
   🎯 [SINGLETON] AdminController instance created
   ```
   Notice: Each controller created **only once** at startup

3. Make multiple requests (browse pages, login, add to cart)
4. **Observe request console logs:**
   ```
   📍 [SINGLETON] HomeController handling request (same instance for all users)
   📍 [SINGLETON] CourseController listing courses
   📍 [SINGLETON] CartController adding to cart
   ```
   Same instance handles all requests!

5. Open application in different browsers/windows (simulate multiple users)
6. Each user gets their own CartService instance (session-scoped)
7. But all users share same controller instances (singleton)

**What Makes It Singleton:**
- Spring IoC Container manages bean lifecycle
- Default scope is singleton (one instance per application)
- Thread-safe concurrent access
- Memory efficient
- Exception: CartService is `@SessionScope`

---

## Console Logging Legend

When running the application, watch for these log patterns:

- `🎯 [SINGLETON]` - Singleton bean creation/usage
- `🎭 [FACADE]` - Facade pattern method calls
- `🔐 [STRATEGY]` - Strategy pattern (password encoding)
- `👁️ [OBSERVER]` - Observer pattern (timestamps)
- `🏭 [FACTORY METHOD]` - Factory method execution
- `🔍 [TEMPLATE METHOD]` - Template method customization
- `📍` - General request handling

---

## Quick Test Checklist

- [ ] **Facade**: Add to cart, view cart, checkout (observe simple method calls)
- [ ] **Strategy**: Register user (password encoded), try switching strategy in code
- [ ] **Observer**: Create course (timestamp auto-set), checkout (purchase timestamp)
- [ ] **Factory Method**: Check startup logs for admin/course creation
- [ ] **Template Method**: Login (observe loadUserByUsername call)
- [ ] **Singleton**: Check startup logs (one instance), make multiple requests (same instance)

---

## Pro Tips

1. **Keep console/terminal visible** - Most pattern demonstrations appear in logs
2. **Test with multiple browsers** - See how singleton controllers serve all users
3. **Try breaking things** - Remove @SessionScope from CartService to see issues
4. **Modify SecurityConfig** - Switch password strategy to see Strategy pattern flexibility
5. **Add more factories** - Create additional @Bean methods in main application
6. **Enable DEBUG logging** - See full Spring Security authentication flow

---

## Success Criteria

You've successfully understood the patterns when you can:

1. ✅ Explain why CartService simplifies controller code (Facade)
2. ✅ Swap password encoders without changing other code (Strategy)
3. ✅ Identify where timestamps are automatically set (Observer)
4. ✅ Add new factory methods for data seeding (Factory Method)
5. ✅ Understand why we only implement loadUserByUsername (Template Method)
6. ✅ Explain why controllers are created once but serve many requests (Singleton)
