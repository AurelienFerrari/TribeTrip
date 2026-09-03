# Backend deployment

Two Render web services, defined in `/render.yaml` (Blueprint), each tracking one branch:

| Render service            | Branch | Spring profile | Neon branch  |
|----------------------------|--------|-----------------|--------------|
| `tribetrip-backend-dev`    | `DEV`  | `dev`           | `dev`        |
| `tribetrip-backend-prod`   | `main` | `prod`          | `production` |

Both build from `backend/Dockerfile` with the repo root as build context (the backend is a
subproject of the root Gradle build — see `settings.gradle.kts` — so it needs the root
wrapper and version catalog to build; `org.gradle.configureondemand=true` in
`gradle.properties` keeps that build from ever touching `:composeApp`, which needs an
Android SDK this image doesn't have).

## Environment variables (set manually per service in the Render dashboard — never committed)

- `SPRING_PROFILES_ACTIVE` — `dev` or `prod`, already set by `render.yaml`, nothing to do.
- `DATABASE_URL` — the raw Neon connection string, exactly as Neon's dashboard gives it:
  `postgresql://<user>:<password>@<host>/<dbname>?sslmode=require`.
  Parsed at startup by `DataSourceConfig.kt` — no reformatting needed, no separate
  username/password variables.
- `JWT_SECRET` — any long random string, **different value in dev and prod**. Only wired
  into config (`jwt.secret` in `application-dev.yml`/`application-prod.yml`) for now — no
  JWT auth flow has been implemented yet, this just provisions the plumbing for it.

## Neon setup

One Neon project, two branches:
- `dev` branch → connection string goes into `tribetrip-backend-dev`'s `DATABASE_URL`.
- `production` branch (or `main`, whatever you name it) → connection string goes into
  `tribetrip-backend-prod`'s `DATABASE_URL`.

Each branch is an isolated Postgres instance (own data, can be reset/branched from the other
independently), which is why `application-dev.yml` uses `ddl-auto: update` (fine to
auto-evolve) while `application-prod.yml` uses `ddl-auto: validate` (never auto-alters the
schema — introduce Flyway/Liquibase before you need real prod migrations).

## Health check

`/actuator/health` is public (see `SecurityConfig.kt`) so Render can poll it without auth;
every other endpoint requires authentication by default.
