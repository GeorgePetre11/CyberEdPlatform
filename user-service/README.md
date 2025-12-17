# User Service

Microservice for user management and authentication in the CyberEd Platform.

## Features

- User registration with password encryption
- User authentication
- User profile management
- Role-based access (USER, ADMIN)
- RESTful API endpoints

## API Endpoints

### POST /api/users/register
Register a new user.

**Request Body:**
```json
{
  "username": "john",
  "password": "password123"
}
```

**Response:** 201 Created
```json
{
  "id": 1,
  "username": "john",
  "roles": ["ROLE_USER"]
}
```

### POST /api/users/login
Authenticate a user.

**Request Body:**
```json
{
  "username": "john",
  "password": "password123"
}
```

**Response:** 200 OK
```json
{
  "id": 1,
  "username": "john",
  "roles": ["ROLE_USER"]
}
```

### GET /api/users/{id}
Get user by ID.

**Response:** 200 OK
```json
{
  "id": 1,
  "username": "john",
  "roles": ["ROLE_USER"]
}
```

### GET /api/users
List all users.

**Response:** 200 OK
```json
[
  {
    "id": 1,
    "username": "admin",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  },
  {
    "id": 2,
    "username": "john",
    "roles": ["ROLE_USER"]
  }
]
```

### DELETE /api/users/{id}
Delete a user.

**Response:** 200 OK
```json
{
  "message": "User deleted successfully"
}
```

### GET /api/users/health
Health check endpoint.

**Response:** 200 OK
```json
{
  "status": "UP",
  "service": "user-service"
}
```

## Running Locally

```bash
cd user-service
mvn spring-boot:run
```

Service will run on http://localhost:8081

## Default Credentials

- **Username:** admin
- **Password:** admin123
- **Roles:** ROLE_ADMIN, ROLE_USER

## Database

Uses H2 in-memory database.

**H2 Console:** http://localhost:8081/h2-console
- **JDBC URL:** jdbc:h2:mem:user_db
- **Username:** sa
- **Password:** (empty)
