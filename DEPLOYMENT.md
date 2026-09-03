# TribeTrip — deployment overview

## Backend (`backend/`) — Render + Neon

Two Render services defined in `render.yaml`, one per branch (`DEV` -> dev, `main` -> prod),
each pointing at its own Neon branch via `DATABASE_URL`. Full details, env var list and the
Neon branch mapping: see [`backend/DEPLOYMENT.md`](backend/DEPLOYMENT.md).

## Web (`composeApp/`) — GitHub Pages (dev) + Cloudflare Pages (prod)

- `.github/workflows/web-deploy-dev.yml` — on push to `DEV`, builds
  `:composeApp:wasmJsBrowserDistribution` and publishes it to GitHub Pages via
  `actions/deploy-pages`. Needs Pages set to "GitHub Actions" as its source in the repo
  settings — no secrets required.
- `.github/workflows/web-deploy-prod.yml` — on push to `main`, builds the same distribution
  and deploys it to a Cloudflare Pages project (`tribetrip-web`) via `cloudflare/pages-action`.
  Needs `CLOUDFLARE_API_TOKEN` and `CLOUDFLARE_ACCOUNT_ID` as GitHub Actions repo secrets.

Both build the same Gradle task (`:composeApp:wasmJsBrowserDistribution`), output directory
`composeApp/build/dist/wasmJs/productionExecutable` — confirmed by running it locally.

## Manual steps

See the end of the conversation this file was generated from for the precise, ordered
checklist (Neon, Render, GitHub, Cloudflare). Nothing above requires an external account to
exist yet — the code/config is in place; only dashboard setup and secrets remain.
