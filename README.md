# ✅ UC18: Spring Security - JWT Authentication & Google OAuth2 for Quantity Measurement

## 📖 Description

UC18 secures the Quantity Measurement Application by introducing **enterprise-grade authentication and authorization** using Spring Security, JWT (JSON Web Tokens), and Google OAuth2.

In UC17, all REST endpoints were publicly accessible — anyone could call `/api/v1/quantities/compare` without any identity. UC18 changes this completely:

- Every API endpoint now requires a valid JWT token
- Users can register and login using **username/password (JWT)**
- Users can also sign in using **Google OAuth2 (Sign in with Google)**
- Both methods return the same JWT token format for consistent API access
- Role-based access control restricts certain endpoints to **ADMIN** users only

UC18 is **fully backward compatible** with UC1–UC17. All business logic and REST endpoints are preserved — they now simply require authentication.

---

## 🎯 Objective 

- Implement **JWT-based authentication** (register + login)
- Implement **Google OAuth2** sign-in with auto user creation
- Protect all `/api/v1/quantities/**` endpoints with JWT
- Implement **role-based access control** (`ROLE_USER`, `ROLE_ADMIN`)
- Centralize authentication logic in `AuthService`
- Integrate with **Swagger UI** for easy token-based API testing
- Persist users and roles using **Spring Data JPA**

---

## 🏗 Updated Architecture
```
Client (Swagger / Postman / Browser)
            ↓
    JwtAuthFilter (intercepts every request)
            ↓
    SecurityConfig (which URLs need auth)
       ↙           ↘
JWT Login        Google OAuth2
  ↓                    ↓
AuthService       OAuth2SuccessHandler
  ↓                    ↓
UserRepository    UserRepository (auto-create user)
  ↓                    ↓
       JWT Token returned
            ↓
    Client sends: Authorization: Bearer <token>
            ↓
    REST Controller processes request
```

---

## 🔐 Authentication Flow

### JWT Flow (username/password)
```
1. POST /auth/register  → User created in DB → JWT returned
2. POST /auth/login     → Password verified  → JWT returned
3. Copy token from response
4. Add to Swagger: Authorize → "Bearer <token>"
5. All API calls now authenticated ✅
```

### Google OAuth2 Flow
```
1. Browser → http://localhost:8080/oauth2/authorization/google
2. Google login page appears
3. User signs in with Gmail
4. OAuth2SuccessHandler creates user in DB (if new)
5. JWT token returned as JSON response
6. Copy token → use in Swagger as "Bearer <token>"
7. All API calls now authenticated ✅
```

---

## 🔹 New Components

### Security Package (`com.app.quantitymeasurement.security`)

| Class | Purpose |
|-------|---------|
| `JwtUtils` | Generates, validates, and parses JWT tokens |
| `JwtAuthFilter` | Intercepts every HTTP request and validates JWT |
| `UserDetailsServiceImpl` | Loads user from DB by email for Spring Security |
| `OAuth2SuccessHandler` | Handles successful Google login, creates user, returns JWT |

### Model Layer

| Class | Purpose |
|-------|---------|
| `User` | Entity with id, email, username, password, fullName, provider, roles |
| `Role` | Entity with `ROLE_USER` and `ROLE_ADMIN` enum values |

### Repository Layer

| Interface | Purpose |
|-----------|---------|
| `UserRepository` | `findByEmail`, `existsByEmail`, `existsByUsername` |
| `RoleRepository` | `findByName(RoleName)` |

### Auth Layer (`com.app.quantitymeasurement.service.auth`)

| Class | Purpose |
|-------|---------|
| `AuthService` | `register()` and `login()` business logic |

### Controller Layer

| Class | Endpoints |
|-------|-----------|
| `AuthController` | `POST /auth/register`, `POST /auth/login` |
| `UserController` | `GET /users/me`, `GET /users/all` (ADMIN only) |

### DTOs

| Class | Purpose |
|-------|---------|
| `RegisterRequest` | Input for registration (fullName, username, email, password) |
| `LoginRequest` | Input for login (email, password) |
| `AuthResponse` | Output with JWT token, user info, and roles |

---

## 🔒 Security Rules

| URL Pattern | Access |
|-------------|--------|
| `POST /auth/register` | Public — no token needed |
| `POST /auth/login` | Public — no token needed |
| `GET /oauth2/authorization/google` | Public — Google login |
| `GET /swagger-ui/**` | Public — API documentation |
| `GET /h2-console/**` | Public — database console |
| `GET /actuator/health` | Public — health check |
| `GET /users/all` | `ROLE_ADMIN` only |
| `GET /users/me` | Any authenticated user |
| `POST /api/v1/quantities/**` | Any authenticated user |
| `GET /api/v1/quantities/**` | Any authenticated user |

---

## 👤 User Entity
```java
@Entity
@Table(name = "users")
public class User {
    private Long id;
    private String username;       // unique
    private String email;          // unique, used as login identifier
    private String password;       // BCrypt encoded (null for Google users)
    private String fullName;
    private String provider;       // "LOCAL" or "GOOGLE"
    private String providerId;     // Google's sub ID (OAuth2 only)
    private boolean enabled;
    private Set<Role> roles;       // ROLE_USER or ROLE_ADMIN
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 🗄 Database Tables Created
```sql
-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    provider_id VARCHAR(255),
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Join table
CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT
);

-- Seeded on startup via data.sql
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
```

---

## ⚙️ New Dependencies Added
```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Google OAuth2 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

---

## ⚙️ application.properties (New Properties)
```properties
# JWT Configuration
app.jwt.secret=QuantityMeasurementAppSecretKey2026VeryLongSecretKeyForHS256Algorithm
app.jwt.expiration-ms=86400000

# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile

# Role seeding
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```

---

## 🚀 How to Run
```bash
# Build
mvn clean compile

# Run
mvn spring-boot:run
```

---

## 🌐 Access Points

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Interactive API docs + testing |
| `http://localhost:8080/auth/register` | Register endpoint |
| `http://localhost:8080/auth/login` | Login endpoint |
| `http://localhost:8080/oauth2/authorization/google` | Google login |
| `http://localhost:8080/users/me` | Current user profile |
| `http://localhost:8080/h2-console` | H2 database console |
| `http://localhost:8080/actuator/health` | Health check |

---

## 🧪 Sample curl Commands

### Register
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Bhumika Shrivas",
    "username": "bhumika",
    "email": "bhumika@test.com",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "bhumika",
  "email": "bhumika@test.com",
  "fullName": "Bhumika Shrivas",
  "roles": ["ROLE_USER"]
}
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "bhumika@test.com",
    "password": "password123"
  }'
```

### Get Profile (with token)
```bash
curl http://localhost:8080/users/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Call Quantity API (with token)
```bash
curl -X POST http://localhost:8080/api/v1/quantities/compare \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
    "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
  }'
```

### Call without token (should get 401)
```bash
curl http://localhost:8080/api/v1/quantities/history/operation/COMPARE
# Returns: 401 Unauthorized
```

---

## 🔑 How to Use JWT in Swagger UI

1. Call `POST /auth/register` or `POST /auth/login`
2. Copy the `token` value from the response
3. Click the **🔒 Authorize** button at the top right of Swagger UI
4. Type: `Bearer <paste_your_token_here>`
5. Click **Authorize** → **Close**
6. All subsequent API calls will automatically include the token ✅

---

## 🌐 Google OAuth2 Setup (Google Cloud Console)

1. Go to [https://console.cloud.google.com](https://console.cloud.google.com)
2. Create new project → **QuantityMeasurementApp**
3. APIs & Services → Library → Enable **Google+ API**
4. APIs & Services → Credentials → Create **OAuth Client ID**
5. Application type: **Web application**
6. Authorized redirect URI:
```
http://localhost:8080/login/oauth2/code/google
```
7. Copy **Client ID** and **Client Secret** → paste into `application.properties`
8. Test: open `http://localhost:8080/oauth2/authorization/google` in browser

---

## 🧠 Spring Security Concepts Learned

| Concept | Implementation |
|---------|---------------|
| JWT generation | `JwtUtils.generateTokenFromEmail()` using `jjwt` library |
| JWT validation | `JwtUtils.validateToken()` — checks signature + expiry |
| JWT filter | `JwtAuthFilter extends OncePerRequestFilter` |
| Stateless sessions | `SessionCreationPolicy.STATELESS` — no server-side sessions |
| Password encoding | `BCryptPasswordEncoder` — industry standard hashing |
| UserDetails | `UserDetailsServiceImpl.loadUserByUsername()` |
| Authentication | `AuthenticationManager.authenticate()` |
| OAuth2 | `oauth2Login().successHandler(OAuth2SuccessHandler)` |
| Role-based access | `@PreAuthorize("hasRole('ADMIN')")` |
| Method security | `@EnableMethodSecurity` on `SecurityConfig` |
| Public URLs | `.requestMatchers(...).permitAll()` |
| Protected URLs | `.anyRequest().authenticated()` |

---

## 📤 Postconditions

- All `/api/v1/quantities/**` endpoints require a valid JWT ✅
- `POST /auth/register` creates user with BCrypt password and ROLE_USER ✅
- `POST /auth/login` authenticates and returns JWT ✅
- Google OAuth2 sign-in auto-creates user and returns JWT ✅
- `GET /users/me` returns current user profile ✅
- `GET /users/all` accessible only to ROLE_ADMIN ✅
- Swagger UI works with Authorize → Bearer token ✅
- H2 console remains accessible without auth ✅
- All UC1–UC17 business logic preserved ✅

---

## 🚀 Architectural Evolution

| Use Case | Capability Added |
|----------|-----------------|
| UC1–UC8 | Length measurement operations |
| UC9 | Weight measurement |
| UC10 | Generic quantity architecture |
| UC11 | Volume measurement |
| UC12 | Subtraction & Division |
| UC13 | Centralized arithmetic logic |
| UC14 | Temperature measurement |
| UC15 | N-Tier architecture |
| UC16 | JDBC database persistence |
| UC17 | Spring Boot REST API + JPA |
| **UC18** | **JWT + Google OAuth2 + Role-based Security** |

---

## 🔥 Key Achievement

UC18 transforms the application from an **open REST API** into a **secured, production-ready service**.

The system now supports:
- Stateless JWT authentication — scales horizontally without session storage
- Google OAuth2 — enterprise-grade social login
- Role-based authorization — fine-grained access control
- BCrypt password hashing — secure credential storage
- Foundation ready for: refresh tokens, email verification, rate limiting, and cloud deployment

---

