# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
and the project follows [Semantic Versioning](https://semver.org/):
**x.y.z**, where `y` increases for new functionality (and resets `z`),
`z` for bug fixes, `x` for breaking changes.

## [Unreleased]

## [v1.0.0] - 2026-09-09

First public release of the Spring Boot API Starter - Free Edition.

### Added

- Java 21, Spring Boot 3.5.16, Maven build with Maven Wrapper (`mvnw` / `mvnw.cmd`)
- Simple layered structure: controller, service, repository, DTOs, mapper
- `Task` resource with full CRUD at `/api/tasks` and correct HTTP status codes (200, 201 + `Location`, 204, 400, 404)
- Java records for request/response DTOs
- Jakarta Validation on request bodies (`@NotBlank`, `@Size`, `@NotNull`)
- Global exception handler with a standardized JSON error payload, including per-field validation errors
- H2 in-memory database with dev-only H2 console at `/h2-console`
- Swagger UI at `/swagger-ui/index.html` and OpenAPI JSON at `/v3/api-docs` (springdoc 2.8.17)
- Tests: service unit tests (Mockito) and MockMvc integration tests against the real context and H2
- README with quick start, endpoint table, error format and curl examples
- `docs/PREMIUM-ROADMAP.md`, `docs/PRODUCT-PAGE-DRAFT.md`, `docs/LAUNCH-CHECKLIST.md`

[Unreleased]: https://github.com/phabdev/spring-boot-api-starter-free/compare/v1.0.0...HEAD
[v1.0.0]: https://github.com/phabdev/spring-boot-api-starter-free/releases/tag/v1.0.0
