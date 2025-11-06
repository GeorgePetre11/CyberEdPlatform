# 🎨 Design Patterns Demo - Quick Start

## What Is This?

A **standalone, runnable Spring Boot application** demonstrating **6 design patterns** extracted from the Cybersecurity Learning Platform. Each pattern is fully functional with detailed logging and documentation.

## 🚀 Quick Start (3 Steps)

### 1. Prerequisites
- Java 17 or higher
- Maven 3.6+

### 2. Run the Application

**On macOS/Linux:**
```bash
cd design-patterns-demo
./run.sh
```

**On Windows:**
```cmd
cd design-patterns-demo
run.bat
```

**Or manually:**
```bash
mvn clean install
mvn spring-boot:run
```

### 3. Access the Application

Open browser: **http://localhost:8080**

**Login Credentials:**
- Admin: `admin` / `demo123`
- User: `user` / `user123`

## 📋 The 6 Patterns

| Pattern | Location | Test Action |
|---------|----------|-------------|
| **🎭 Facade** | CartService.java | Add to cart → View cart → Checkout |
| **🔄 Strategy** | SecurityConfig.java | Register new user (password encrypted) |
| **👁️ Observer** | Course.java, Purchase.java | Create course (timestamp auto-set) |
| **🏭 Factory Method** | DesignPatternsDemoApplication.java | Check startup logs |
| **📋 Template Method** | CustomUserDetailsService.java | Login (check console logs) |
| **🔷 Singleton** | All Controllers | Check startup (created once) |

## 🧪 Quick Test Flow

1. **Start app** → See Factory Method logs create admin & courses
2. **Login** → See Template Method load user
3. **Register new user** → See Strategy encrypt password
4. **Browse courses** → Notice Observer-set timestamps
5. **Add to cart** → See Facade simplify operations
6. **Checkout** → See Observer auto-set purchase timestamp
7. **Admin panel** → Create course, see patterns interact

## 📚 Documentation Files

- **README.md** - Overview and setup
- **PATTERNS_DOCUMENTATION.md** - Detailed pattern explanations
- **TESTING_GUIDE.md** - Step-by-step testing instructions

## 💡 Console Logs

Watch terminal for pattern logs:
```
🏭 [FACTORY METHOD] Creating admin user...
🎯 [SINGLETON] HomeController instance created
🔐 [STRATEGY] Encoding password with current strategy
🔍 [TEMPLATE METHOD] Loading user: admin
🎭 [FACADE] Using CartService.add()
👁️ [OBSERVER] Course saved with auto-set timestamp!
```

## 🎯 Key Features

✅ **No External Database** - Uses H2 in-memory (auto-configured)  
✅ **Automatic Data Seeding** - 3 courses + 2 users created at startup  
✅ **Fully Functional** - Not toy examples, real implementations  
✅ **Extensive Logging** - See patterns in action via console  
✅ **Clean Code** - Well-documented, follows best practices  
✅ **Educational** - Perfect for learning/teaching patterns  

## 🔧 Troubleshooting

**Port 8080 already in use:**
```bash
# Edit application.properties
server.port=8081
```

**Build fails:**
```bash
# Clean and rebuild
mvn clean
mvn install -U
```

**H2 Console (for debugging):**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:patternsdb`
- Username: `sa`
- Password: (leave empty)

## 📖 Learn More

Each Java class has extensive comments explaining:
- **What** the pattern does
- **Why** it's used
- **How** it works
- **Benefits** it provides

Look for `// PATTERN:` comments throughout the codebase!

## 🎓 Educational Use

Perfect for:
- Design patterns course projects
- Software engineering assignments
- Interview preparation
- Code review examples
- Teaching material

---

**Ready to explore? Run `./run.sh` and visit http://localhost:8080!** 🚀
