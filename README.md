# CyberEdPlatform - Milestone 5: Microservices with Message Queue & CI/CD

An educational platform built with Spring Boot microservices architecture, featuring **RabbitMQ message queue integration** for asynchronous communication and a **GitHub Actions CI/CD pipeline** for automated builds and deployments.

---

## 🏗️ Architecture Overview

### Microservices (4 Services)
1. **User Service** (Port 8081) - User authentication and management
2. **Course Service** (Port 8082) - Course catalog and inventory
3. **Order Service** (Port 8083) - Order processing and checkout
4. **Forum Service** (Port 8084) - Discussion forums with PostgreSQL

### Infrastructure Services
- **RabbitMQ** (Ports 5672, 15672) - Message broker for async communication
- **PostgreSQL** (Port 5432) - Database for Forum Service

---

## 📨 Message Queue Integration

### Why RabbitMQ?
- **Decoupling**: Order Service no longer waits for Course Service to update inventory
- **Fault Tolerance**: If Course Service is down, messages are queued and processed when it recovers
- **Scalability**: Can add multiple Course Service instances to process inventory updates in parallel
- **Reliability**: Durable queues ensure messages aren't lost

### Architecture Flow
```
Order Service → RabbitMQ (order.exchange) → Course Service
   (Publisher)      [Queue: order.inventory.queue]     (Consumer)
```

**What happens when you place an order:**
1. Order Service validates user and course availability
2. Order is saved to database
3. `OrderPlacedEvent` is published to RabbitMQ asynchronously
4. Order Service immediately returns response (fast!)
5. Course Service consumes event from queue and updates inventory
6. If Course Service is down, message waits in queue

**Message Format (OrderPlacedEvent):**
```json
{
  "orderId": 1,
  "userId": 2,
  "courseId": 1,
  "courseName": "Introduction to Cybersecurity",
  "totalPrice": 49.99,
  "timestamp": "2025-12-16T10:30:00",
  "quantityChange": -1
}
```

### Access RabbitMQ Management UI
```
URL: http://localhost:15672
Username: guest
Password: guest
```

---

## 🚀 Quick Start Guide

### 1. Start All Services

```bash
cd CyberEdPlatform-milestone4-microservices
docker-compose up --build -d
```

Wait for all services to start (about 4-5 minutes). You should see:
```
rabbitmq         | RabbitMQ is running
user-service     | User Service is running on port 8081
course-service   | Course Service is running on port 8082
order-service    | Order Service is running on port 8083
forum-service    | Forum Service is running on port 8084
postgres-db      | database system is ready to accept connections
```

### 2. Verify Services

```bash
# User Service
curl http://localhost:8081/api/users/health

# Course Service
curl http://localhost:8082/api/courses/health

# Order Service
curl http://localhost:8083/api/orders/health

# Forum Service
curl http://localhost:8084/api/forum/health
```

### 3. Test Message Queue (Async Order Processing)

```bash
# 1. Register a user
curl -X POST http://localhost:8081/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'

# 2. Check initial course inventory
curl http://localhost:8082/api/courses

# 3. Place an order (triggers async message to RabbitMQ)
curl -X POST http://localhost:8083/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"courseId":1}'

# 4. Check course inventory again (should decrease by 1 after processing)
curl http://localhost:8082/api/courses

# 5. View RabbitMQ message flow in Management UI
# Open: http://localhost:15672 (guest/guest)
# Navigate to Queues → order.inventory.queue
```

**Expected Behavior:**
- Order is created instantly (Order Service doesn't wait)
- Inventory updates asynchronously within 1-2 seconds
- Check logs: `docker logs course-service` to see message consumption

### 4. Test Forum Service

```bash
# Create a forum post
curl -X POST http://localhost:8084/api/forum/posts \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"title":"My First Post","content":"Hello from the forum!"}'

# Add a comment
curl -X POST http://localhost:8084/api/forum/comments \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"postId":1,"content":"Welcome to the forum!"}'

# View all posts
curl http://localhost:8084/api/forum/posts
```

### 5. Test with Postman
Import the Postman collection from `postman/CyberEd-Microservices.postman_collection.json`

---

## 🔄 CI/CD Pipeline (GitHub Actions)

### What It Does
The CI/CD pipeline automatically:
1. **Builds** all 4 microservices with Maven
2. **Runs tests** for each service
3. **Builds Docker images** for all services
4. **Deploys** to local Docker environment
5. **Runs health checks** on all services
6. **Executes integration tests** (user registration, course retrieval)

### Pipeline Configuration
Located at: `.github/workflows/ci-cd.yml`

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop`

**Pipeline Stages:**

| Stage | Actions |
|-------|---------|
| **Build & Test** | Maven clean, package, and unit tests for all services |
| **Docker Build** | Build all Docker images using docker-compose |
| **Deploy** | Start services with docker-compose up |
| **Health Checks** | Verify all services are responsive |
| **Integration Tests** | Test user registration, course API |

### How to Use

**Locally (simulate CI/CD):**
```bash
# Run the same steps as GitHub Actions
mvn clean package -f user-service/pom.xml
mvn clean package -f course-service/pom.xml
mvn clean package -f order-service/pom.xml
mvn clean package -f forum-service/pom.xml

docker-compose build
docker-compose up -d

# Wait for services to start (60 seconds)
sleep 60

# Run health checks
curl http://localhost:8081/api/users/health
curl http://localhost:8082/api/courses/health
curl http://localhost:8083/api/orders/health
curl http://localhost:8084/api/forum/health
```

**On GitHub:**
1. Push code to your repository
2. GitHub Actions automatically runs the pipeline
3. View results at: `https://github.com/<your-username>/<repo>/actions`
4. Green checkmark = All tests passed ✅
5. Red X = Build/test failure ❌

**Optional: Push to Docker Hub**
Uncomment the Docker Hub section in `.github/workflows/ci-cd.yml` and add these secrets to your GitHub repository:
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

---

## 📊 Technology Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 3.4.5 |
| Language | Java 17 |
| Build Tool | Maven |
| Message Queue | RabbitMQ 3.13 |
| Databases | H2 (3 services), PostgreSQL 16 (Forum) |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| API Testing | Postman |

---

## 🧪 Troubleshooting

### Services won't start
```bash
# Check if ports are in use (Windows PowerShell)
netstat -ano | findstr :8081
netstat -ano | findstr :5672
netstat -ano | findstr :15672

# Stop conflicting processes
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8081
kill -9 <PID>
```

### RabbitMQ not receiving messages
```bash
# Check RabbitMQ is running
docker logs rabbitmq

# Verify queue exists in Management UI
# http://localhost:15672 → Queues tab → order.inventory.queue

# Check Order Service logs
docker logs order-service | grep "Published OrderPlacedEvent"

# Check Course Service logs
docker logs course-service | grep "Received order placed event"
```

### Rebuild after code changes
```bash
docker-compose down -v
docker-compose up --build
```

### View service logs
```bash
docker logs user-service
docker logs course-service
docker logs order-service
docker logs forum-service
docker logs rabbitmq
docker logs postgres-db
```

### Clear all data and restart fresh
```bash
docker-compose down -v  # -v removes volumes
docker system prune -a  # Remove all unused images
docker-compose up --build -d
```

---

## 🎯 Benefits Demonstrated

### Message Queue Benefits
✅ **Decoupling**: Order Service doesn't wait for Course Service  
✅ **Fault Tolerance**: Messages persist if Course Service crashes  
✅ **Scalability**: Can scale Course Service horizontally  
✅ **Async Processing**: Faster response times for users  

### CI/CD Pipeline Benefits
✅ **Automation**: No manual build/test/deploy steps  
✅ **Quality Assurance**: Automated tests catch bugs early  
✅ **Consistency**: Same build process every time  
✅ **Fast Feedback**: Know if code breaks within minutes  

---

## 📚 Project Structure

```
CyberEdPlatform-milestone4-microservices/
├── .github/workflows/
│   └── ci-cd.yml                    # GitHub Actions pipeline
├── user-service/                    # User authentication service
├── course-service/                  # Course catalog (RabbitMQ consumer)
├── order-service/                   # Order processing (RabbitMQ publisher)
├── forum-service/                   # Forum with PostgreSQL
├── docker-compose.yml               # Multi-service orchestration
├── postman/                         # API test collection
└── README.md                        # This file
```

---

## 📖 Additional Resources

- [RabbitMQ Management UI](http://localhost:15672) (guest/guest)
- [Spring AMQP Documentation](https://spring.io/projects/spring-amqp)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- Postman Collection: `postman/CyberEd-Microservices.postman_collection.json`

---

## 👨‍💻 Development Workflow

1. Make code changes in any service
2. Commit and push to GitHub
3. GitHub Actions runs automatically
4. Check pipeline results in Actions tab
5. If green ✅, code is ready for review/merge
6. If red ❌, fix issues and push again
