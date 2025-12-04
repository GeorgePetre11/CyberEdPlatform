# Quick Start Guide

## 1. Start Services

```bash
cd CyberEdPlatform
docker-compose up --build -d
```

Wait for all services to start (about 3-4 minutes). You should see:
```
user-service     | User Service is running on port 8081
course-service   | Course Service is running on port 8082
order-service    | Order Service is running on port 8083
forum-service    | Forum Service is running on port 8084
postgres-db      | database system is ready to accept connections
```

## 2. Verify Services

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

## 4. Test with Postman
**Services won't start:**
```bash
# Check if ports are in use
lsof -i :8081
lsof -i :8082
lsof -i :8083
lsof -i :8084
lsof -i :5432

# Kill conflicting processes
kill -9 <PID>
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
**View logs:**
```bash
docker logs user-service
docker logs course-service
docker logs order-service
docker logs forum-service
docker logs postgres-db
```ker logs course-service
docker logs order-service
```
