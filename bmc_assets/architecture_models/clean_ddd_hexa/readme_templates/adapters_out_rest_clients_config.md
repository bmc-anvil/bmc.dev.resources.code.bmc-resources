# REST Client Config

Contains:

- HTTP client setup, base URLs, interceptors, auth.

Purpose:

- Centralize client configuration concerns.

Guidance:

- Do:
    - Externalize configuration and keep it environment-aware.
    - Implement observability (tracing/metrics).
- Don’t:
    - Duplicate client setup across modules.
    - Mix business rules with configuration.
