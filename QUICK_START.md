# Quick Start Guide

## 1. Start Services

```bash
cd CyberEdPlatform-Microservices
docker-compose up --build
```

Wait for all services to start (about 2-3 minutes). You should see:
```
user-service     | User Service is running on port 8081
course-service   | Course Service is running on port 8082
order-service    | Order Service is running on port 8083
```

## 2. Verify Services

```bash
# User Service
curl http://localhost:8081/api/users/health

# Course Service
curl http://localhost:8082/api/courses/health

# Order Service
curl http://localhost:8083/api/orders/health
```

## 3. Quick Test

```bash
# Register a user
curl -X POST http://localhost:8081/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'

# Get courses
curl http://localhost:8082/api/courses

# Purchase a course
curl -X POST http://localhost:8083/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"courseId":1}'
```

## 4. Test with Postman

1. Open Postman
2. Import `postman/CyberEd-Microservices.postman_collection.json`
3. Run the collection

## 5. Stop Services

```bash
docker-compose down
```

## Troubleshooting

**Services won't start:**
```bash
# Check if ports are in use
lsof -i :8081
lsof -i :8082
lsof -i :8083

# Kill conflicting processes
kill -9 <PID>
```

**Rebuild after changes:**
```bash
docker-compose down
docker-compose up --build
```

**View logs:**
```bash
docker logs user-service
docker logs course-service
docker logs order-service
```
