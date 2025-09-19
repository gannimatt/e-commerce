# E‑Commerce API (Spring Boot)

A clean, production‑style REST API for a simple e‑commerce domain. Built to showcase solid Java fundamentals (Spring Boot, Security/JWT, JPA/Hibernate, testing), good project hygiene (DTOs/mappers, exception handling), and DevOps basics (Docker, CI/CD).

## Highlights
- Spring Boot 3 (Java 21)
- JWT authentication with role‑based access (ADMIN, CUSTOMER)
- Domain: Products, Categories, Cart, Orders
- Validation, DTO mapping, and global error handling
- Swagger/OpenAPI UI for interactive docs
- PostgreSQL in runtime; H2 for tests
- Dockerized (app + Postgres) with `docker-compose`
- GitHub Actions CI (build + tests) and CD (image publish to GHCR)

## Tech Stack
- Runtime: Spring Boot (Web, Security, Validation), Spring Data JPA, Hibernate
- Auth: JWT (jjwt) + custom filter
- DB: PostgreSQL (runtime), H2 (tests)
- Docs: springdoc‑openapi (Swagger UI)
- Build: Maven
- DevOps: Docker, Docker Compose, GitHub Actions (CI/CD)
- Testing: JUnit 5, Mockito, Spring Test (WebMvcTest/Data/Boot)

## Project structure (key packages)
```
src/main/java/com/github/gannimatt/ecommerce
  ├─ config/        # Security, JWT, OpenAPI, seeding
  ├─ controller/    # REST controllers
  ├─ dto/           # Request/Response records
  ├─ entity/        # JPA entities + enums
  ├─ exception/     # GlobalExceptionHandler, NotFound
  ├─ mapper/        # DTO ↔ entity mappers
  ├─ repository/    # Spring Data repositories
  └─ service/       # Interfaces + impl (business logic)
```

## API overview
- Auth:
  - POST `/api/auth/register` → returns JWT
  - POST `/api/auth/login` → returns JWT
- Products:
  - GET `/api/products` (public, paged search)
  - GET `/api/products/{id}` (public)
  - POST/PUT/DELETE `/api/products/**` (ADMIN)
- Categories:
  - GET `/api/categories`, `/api/categories/{id}`, `/api/categories/slug/{slug}` (public)
  - POST/PUT/DELETE `/api/categories/**` (ADMIN)
- Cart (auth):
  - GET `/api/cart`
  - POST `/api/cart/items`
  - DELETE `/api/cart/items/{productId}`
  - DELETE `/api/cart`
- Orders (auth):
  - POST `/api/orders/checkout`
  - GET `/api/orders`, `/api/orders/{id}`

OpenAPI/Swagger UI: `http://localhost:8080/swagger-ui.html`

## Security
- JWT via `Authorization: Bearer <token>` header
- Roles based on `User.roles` (ROLE_ADMIN, ROLE_CUSTOMER)
- Public reads: `GET /api/**` (browsing)
- Writes on products/categories: ADMIN only
- Cart/Order actions: any authenticated user

## Getting started

### 1) Environment variables
Use a `.env` file (not committed) at the project root or set env vars in your shell/CI:
```
DB_USERNAME=postgres
DB_PASSWORD=postgres
APP_JWT_SECRET=<32+ chars random secret>
APP_JWT_EXPIRATION_MS=3600000

# Optional admin seeding on startup
APP_SEED_ADMIN_ENABLED=true
APP_SEED_ADMIN_EMAIL=admin@local
APP_SEED_ADMIN_PASSWORD=admin123
```

### 2) Run locally (IntelliJ / Maven)
- Ensure Postgres is running on `localhost:5432` with the provided credentials
- Configure `.env` (or IDE env vars)
- Run from IDE or:
```
mvn spring-boot:run
```
App URL: `http://localhost:8080` → open Swagger.

### 3) Run with Docker (no local JDK/Postgres needed)
- Ensure Docker Desktop is running
- Copy `.env.example` → `.env` and fill values (especially `APP_JWT_SECRET`)
- Build & start:
```
docker compose up --build
```
- App: `http://localhost:8080/swagger-ui.html`
- DB: exposed at `localhost:5432`

If you change DB data and want a clean state:
```
docker compose down
# remove persisted data volume (resets DB)
docker volume rm e-commerce_pgdata
```

## Using the API quickly
- Register or login to get a JWT token
- Click “Authorize” in Swagger and paste the token (no `Bearer ` prefix needed in UI)
- As ADMIN, create categories/products, then as CUSTOMER add to cart and checkout
- Admin seeding: enable `APP_SEED_ADMIN_ENABLED=true` to auto‑create an admin user on startup

## Configuration
- Main config: `src/main/resources/application.yml`
  - Reads DB creds and JWT config from env vars
  - Defaults: Postgres URL `jdbc:postgresql://localhost:5432/postgres`
- Test profile: `src/main/resources/application-test.yml`
  - H2 in‑memory DB, Hibernate create‑drop, H2 dialect
  - Test JWT secret/expiry provided for CI and local tests

## Testing
- Run all tests:
```
mvn -B -ntp clean verify
```
- Context tests use `@ActiveProfiles("test")` to load `application-test.yml`
- Web slice tests mock security filter where needed

## CI/CD
- CI: `.github/workflows/ci.yml`
  - On every push/PR: builds with Temurin JDK 21, runs tests with `SPRING_PROFILES_ACTIVE=test`
  - Uploads surefire reports on failure
- CD: `.github/workflows/docker-publish.yml`
  - On push to `main`/`master` or tags `v*`: builds and pushes Docker image to GHCR
  - Image name: `ghcr.io/<owner>/ecommerce` (metadata action adds `latest`, `sha`, and tag refs)

## Design notes
- DTOs & mappers keep controllers/services decoupled from JPA entities
- Validation annotations on DTOs ensure clean error responses
- `GlobalExceptionHandler` converts exceptions into consistent `ApiError` payloads
- Cart totals computed from current product prices; Order snapshots unit prices at checkout
- Security filter (`JwtAuthFilter`) parses token and sets authorities from user roles

## What I would build next
- Pagination and sorting examples in README
- Basic rate limiting / CORS fine‑tuning
- Integration tests with Testcontainers (Postgres)
- Simple frontend (React/Angular) to consume this API

## License
[MIT](LICENSE)
