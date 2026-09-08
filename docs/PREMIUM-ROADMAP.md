# Premium Edition Roadmap

This document describes what the **Spring Boot API Starter Kit Premium** will contain and why those parts are not in the free repository. It contains no premium code.

## Premium Edition goals

The Free Edition shows how a clean Spring Boot API is laid out. The Premium Edition removes the two or three days of setup that every real project needs before the first feature can be written:

- authentication and authorization that work out of the box;
- a real database with migrations and a reproducible local environment;
- environment-specific configuration ready for a deploy;
- documentation that explains how to adapt the kit to a client project, not just how to run it.

The goal is not clever code. The goal is a base you can hand to a junior developer, or to yourself on a Monday morning, and have a working, secured, containerized API by lunch.

## Features planned

### Security

- JWT authentication (access token) with a clean filter chain
- Refresh token flow with rotation and revocation
- `register`, `login`, `logout`, `refresh` endpoints
- Password hashing with BCrypt
- Role-based access control (`USER`, `ADMIN`) with method-level security
- Consistent `401` / `403` responses using the same `ApiErrorResponse` format as the Free Edition

### Persistence and environments

- PostgreSQL as the primary database
- Flyway migrations (versioned SQL, baseline included)
- `dev`, `test`, `prod` Spring profiles with sensible defaults
- H2 kept for fast tests only
- Dockerfile (multi-stage, non-root user) and `docker-compose.yml` with PostgreSQL

### Quality

- Broader test suite: security, repository, integration tests with Testcontainers
- Structured request logging with correlation id
- Swagger UI with JWT authorization button and documented security schemes

### Documentation and tooling

- Detailed premium README
- Guide: "How to adapt this kit to a client project"
- Deploy checklist (environment variables, secrets, database, reverse proxy, health checks)
- Postman collection covering all endpoints, including auth flows
- Advanced curl examples
- CHANGELOG

## Why these features are premium

Each item above is something developers rebuild from scratch on most projects, and each one is easy to get slightly wrong:

- **Security** is the area with the highest cost of mistakes. A working JWT + refresh token setup with sane defaults is worth more than any tutorial.
- **Migrations and profiles** are boring and repetitive, yet they are exactly what turns a demo into something deployable.
- **Docker and PostgreSQL** make the project reproducible for a team and for a client, not just on one laptop.
- **Documentation for adaptation** is what a freelancer needs when the kit has to become a client's project by Friday.

The Free Edition stays useful on its own: it is a complete, correct CRUD API with validation, error handling and OpenAPI. It just stops where production concerns begin.

## Suggested pricing

| Edition | Price | Distribution |
|---------|-------|--------------|
| Free | 0 EUR | Public GitHub repository, MIT |
| Premium | 29 EUR (launch price) | Private repository access or zip download |

Price may be adjusted after the first feedback round. Early buyers keep access to updates of the same major version.

## Premium license (draft)

The Premium Edition will use a simple proprietary license:

```text
The Premium Edition can be used by the individual buyer in personal and commercial projects.
Redistribution, resale, public sharing, or publishing the source code is not allowed.
```

One purchase, one developer, unlimited projects. Team licenses may be added later.

## Future Pro Bundle

Planned, not in development yet:

```text
Spring Boot + Angular Fullstack Starter Kit
```

- Premium Spring Boot backend
- Angular admin frontend (login, user management, base dashboard)
- Full Docker Compose setup (API, database, frontend)
- Full-stack documentation

Indicative price: 79-99 EUR.

Product roadmap, in order:

1. `spring-boot-api-starter-free` (this repository)
2. `spring-boot-api-starter-premium`
3. `angular-admin-starter-free`
4. `angular-admin-starter-premium`
5. `freelance-dev-toolkit`
6. `spring-angular-fullstack-starter-pro`

## Support boundaries

- Free Edition: issues and pull requests on GitHub, best effort, no response-time commitment.
- Premium Edition: email support for setup problems and bugs in the kit itself. No consulting, no custom feature development, no debugging of your own code built on top of the kit.
- Updates: bug fixes and dependency updates for the current major version. Major upgrades (for example a new Spring Boot major) may be released as a new version.
