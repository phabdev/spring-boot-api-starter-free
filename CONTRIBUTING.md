# Contributing - spring-boot-api-starter-free

Thanks for taking the time to contribute. This is a small, deliberately
minimal project: the best contributions keep it that way.

## Scope of the Free Edition

This repository is the free, open-core part of a two-tier product. It
intentionally does **not** include authentication, JWT, roles,
PostgreSQL, Docker, database migrations, production profiles, CI/CD,
structured logging or a frontend. Pull requests adding those will be
closed with a pointer to `docs/PREMIUM-ROADMAP.md`. Bug fixes, clearer
docs, better tests and small quality improvements are very welcome.

## Git Flow

The project follows the classic Git Flow (Vincent Driessen):

- **`feature/*`** / **`fix/*`**: branch from `develop`, PR back into
  `develop`.
- **`release/x.y.z`**: branch from `develop` to prepare a version (minor
  bug fixes and version bump only, no new features). When done: PR into
  `master` (tagged), then merge the release branch into `develop` too.
- **`hotfix/*`**: branch from `master` for critical bugs already
  released. Explicit merge into both `master` (new patch tag) and
  `develop`.
- `master` always reflects the latest released state and is tagged on
  every release/hotfix. Every tag has a matching GitHub release.

## Versioning

Semantic Versioning `x.y.z`:

- `y` increases for new functionality (`z` resets to 0)
- `z` increases for bug fixes (same `x.y`)
- `x` increases for breaking changes (`y` and `z` reset to 0)

The version lives in three places that must change in the same release
commit: `pom.xml` (`<version>`), the top entry of `CHANGELOG.md`, and
the `.version(...)` in `OpenApiConfig`.

## Commits

[Conventional Commits](https://www.conventionalcommits.org/), in
English: `<type>(<optional scope>): <description>`.

Main types: `feat`, `fix`, `docs`, `chore`, `refactor`, `test`,
`style`, `ci`.

## Lightweight process for documentation-only changes

When a change touches **only** `.md` files (no code, no dependencies,
no build involved) the full release cycle is not needed:

1. Branch `docs/<short-name>` from `develop`.
2. Commit and push.
3. **Open a PR directly into `master` and another into `develop`** (two
   separate PRs from the same branch, in any order), **without** an
   intermediate `release/x.y.z` branch.
4. **No version bump, no new `CHANGELOG.md` entry**: a roadmap or
   policy update is not a product release.
5. No build required (no code touched), only consistency between the
   `.md` files (internal links, references to issues/versions still
   valid).

If even one code file is part of the change, the normal process applies
(feature/fix + full release).

## Process for a new feature or fix

1. **Open a GitHub issue before writing code**: goal, scope, decisions
   taken. Wait for explicit confirmation before implementing anything
   non-trivial, especially anything that might cross the free/premium
   boundary.
2. Branch `feature/*` or `fix/*` from `develop`. Reference the issue
   number (`#N`, without "Closes") in the body of the PR into `develop`.
3. Implementation plus full verification (see below).
4. **Security check before the PR**: input validation on every request
   body, no stack traces or internals leaked in error responses, no
   secrets in configuration.
5. PR into `develop` **without** "Closes #N" in the body (merging into
   `develop` would not auto-close it anyway).
6. After the merge the issue stays open: it is closed at release time.
7. At release: branch `release/x.y.z`, PR into `master` with **"Closes
   #N" for every issue resolved** in the body. Merging into `master`
   closes the issues automatically. Then merge the release branch into
   `develop` and tag the `master` commit.
8. **Update `CHANGELOG.md` and `docs/STATO_SVILUPPO.md`** as part of
   every release/hotfix, not afterwards "when there is time".

## Verification before every commit/PR

Toolchain: JDK 21 and Maven 3.9+, or the included wrapper.

```bash
./mvnw clean test
./mvnw spring-boot:run
```

With the application running, check that these respond:

- `http://localhost:8080/api/tasks`
- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`
- `http://localhost:8080/h2-console`

and that the curl examples in the README behave as documented: `201`
with a `Location` header on create, `400` with `fieldErrors` on
invalid input, `404` on an unknown id, `204` on delete.

- Clean up every temporary artifact generated during verification,
  **processes included**: a Java process left listening on port 8080
  keeps serving the old build and produces "defects" that do not exist.

### When a check fails

**Locate the defect before touching the code.** A red check may point
to a problem in the application or in the tool that checks it, and the
difference is never obvious. Rule: **never change application code to
make a check pass** without first proving where the defect is (call
the API directly, read the logs, isolate the layer), then fix what is
actually broken.

### What unit tests do not catch

For changes that cross several layers, unit tests are not enough
because they typically mock the very piece in the middle. At least one
full pass through the real flow (`spring-boot:run` plus real HTTP
calls, or the MockMvc integration test) is required before declaring a
change ready.

## Using the GitHub API efficiently

- **"Closes #N" only in the release PR into `master`** (never in the
  feature/fix PR into `develop`).
- **Do not verify an action you just performed with a separate read
  call** when the tool already confirmed the outcome.
- **Do not hammer retries** on a rate limit: wait, or continue with
  local work in the meantime.
- Group GitHub calls by phase.

## Notes for assisted or sandboxed environments

Creating git tags and deleting remote branches may be blocked by the
proxy of a remote development environment. In that case those commands
must be handed to a human to run locally, **with the explicit commit
SHA**, preceded by `git fetch origin master` (without the fetch,
`git tag -a <version> <sha>` fails with "not a valid object name" when
the commit is not yet in the local clone).
