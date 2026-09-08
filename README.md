# Spring Boot API Starter - Free Edition

A clean and minimal Spring Boot REST API starter template for developers who want to start fast without messy boilerplate.

It is a small, readable, runnable project: one `Task` resource with a full CRUD, request/response DTOs, validation, a standard error format and Swagger UI. Nothing more. Clone it, run it, read it in fifteen minutes, then replace `Task` with your own domain.

## What's included

- Java 21, Spring Boot 3.5, Maven
- Simple layered structure: controller, service, repository, DTOs, mapper
- REST API example (`/api/tasks`) with correct HTTP status codes
- Java records for request/response DTOs
- Jakarta Validation on request bodies
- Global exception handler with a standardized JSON error response
- H2 in-memory database (no Docker, no external services)
- Swagger UI / OpenAPI via springdoc
- Minimal but real tests: service unit tests and MockMvc integration tests

## What's not included

This free edition intentionally does not include authentication, JWT, refresh tokens, roles, PostgreSQL, Docker, database migrations, production profiles, CI/CD, structured logging, or a frontend.

Those are part of the Premium Edition. See [docs/PREMIUM-ROADMAP.md](docs/PREMIUM-ROADMAP.md).

## Requirements

- JDK 21
- Maven 3.9+, or use the included Maven Wrapper (`./mvnw` on Linux/macOS, `mvnw.cmd` on Windows) and skip installing Maven

## Quick start

```bash
git clone https://github.com/phabdev/spring-boot-api-starter-free.git
cd spring-boot-api-starter-free
mvn clean test
mvn spring-boot:run
```

Without a local Maven installation:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Once the application is running:

| What | URL |
|------|-----|
| API | http://localhost:8080/api/tasks |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| H2 console (dev only) | http://localhost:8080/h2-console |

H2 console credentials: JDBC URL `jdbc:h2:mem:apistarter`, user `sa`, empty password.

The database is in-memory: data is lost every time the application stops. That is intentional for a starter.

## Project structure

```text
com.phabdev.apistarter
├── SpringBootApiStarterApplication.java
├── task
│   ├── TaskItem.java              JPA entity (table "tasks")
│   ├── TaskStatus.java            TODO | IN_PROGRESS | DONE
│   ├── TaskPriority.java          LOW | MEDIUM | HIGH
│   ├── TaskRepository.java        Spring Data JPA repository
│   ├── TaskService.java           Application logic, transactions
│   ├── TaskController.java        REST endpoints
│   ├── dto
│   │   ├── CreateTaskRequest.java
│   │   ├── UpdateTaskRequest.java
│   │   └── TaskResponse.java
│   └── mapper
│       └── TaskMapper.java        Entity <-> DTO, hand-written
├── common
│   ├── error
│   │   ├── ApiErrorResponse.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   └── web
│       └── ApiPaths.java
└── config
    └── OpenApiConfig.java
```

Feature code lives in one package per resource (`task`). Cross-cutting code lives in `common`. Adding a second resource means adding a second package next to `task`, not touching the rest.

## API endpoints

| Method | Path | Success | Errors |
|--------|------|---------|--------|
| GET | `/api/tasks` | 200 OK, list of tasks | |
| GET | `/api/tasks/{id}` | 200 OK | 404 |
| POST | `/api/tasks` | 201 Created, `Location` header | 400 |
| PUT | `/api/tasks/{id}` | 200 OK | 400, 404 |
| DELETE | `/api/tasks/{id}` | 204 No Content | 404 |

### Request bodies

`POST /api/tasks`

| Field | Type | Rules |
|-------|------|-------|
| `title` | string | required, max 120 |
| `description` | string | optional, max 1000 |
| `priority` | `LOW` \| `MEDIUM` \| `HIGH` | optional, defaults to `MEDIUM` |

New tasks always start with status `TODO`.

`PUT /api/tasks/{id}` (full replacement)

| Field | Type | Rules |
|-------|------|-------|
| `title` | string | required, max 120 |
| `description` | string | optional, max 1000 |
| `status` | `TODO` \| `IN_PROGRESS` \| `DONE` | required |
| `priority` | `LOW` \| `MEDIUM` \| `HIGH` | required |

### Error format

Every error uses the same shape:

```json
{
  "timestamp": "2026-09-09T18:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/tasks",
  "fieldErrors": [
    { "field": "title", "message": "must not be blank" }
  ]
}
```

`fieldErrors` is present only for validation errors.

## Example requests

Create a task:

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Build the free starter kit",
    "description": "Create the public open-core version",
    "priority": "HIGH"
  }'
```

List tasks:

```bash
curl http://localhost:8080/api/tasks
```

Get one task:

```bash
curl http://localhost:8080/api/tasks/1
```

Update a task:

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Build the free starter kit",
    "description": "Published on GitHub",
    "status": "DONE",
    "priority": "HIGH"
  }'
```

Delete a task:

```bash
curl -i -X DELETE http://localhost:8080/api/tasks/1
```

Trigger a validation error:

```bash
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{ "title": "" }'
```

## Running the tests

```bash
mvn clean test
```

- `TaskServiceTest`: unit tests for create/get/update/delete with a mocked repository.
- `TaskControllerIntegrationTest`: MockMvc tests against the real application context and H2, covering 201, 400, 404 and the full lifecycle.

## Adapting it to your project

1. Rename the base package and the application class.
2. Replace `task` with your first real resource, keeping the same layout.
3. Keep `common/error` as it is: the error format is domain-agnostic.
4. Swap H2 for a real database when you need persistence. That, along with migrations and profiles, is exactly where the Premium Edition starts.

## Premium Edition

Want the complete version with JWT authentication, refresh tokens, role-based access control, PostgreSQL, Docker Compose, Flyway migrations, `dev`/`test`/`prod` profiles, a Postman collection, a deploy checklist and a production-ready structure?

Premium Edition: coming soon.

> Premium Edition link coming soon.

What is planned and why it is premium: [docs/PREMIUM-ROADMAP.md](docs/PREMIUM-ROADMAP.md).

## License

MIT for the Free Edition. See [LICENSE](LICENSE).

The Premium Edition will ship under a separate proprietary license.

## Author

Fabrizio Ferrante, full stack developer (Java/Spring, Angular) working as PHABDEV.
