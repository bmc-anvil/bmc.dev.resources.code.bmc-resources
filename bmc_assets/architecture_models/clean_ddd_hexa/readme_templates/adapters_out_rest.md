# Outbound REST

Contains:

- REST clients with config, DTOs, error handling, and mappers.

Purpose:

- Call external HTTP APIs and map results to app/domain models.

Guidance:

- Do:
    - Apply timeouts, retries, circuit breakers.
    - Translate HTTP errors to application exceptions.
- Don’t:
    - Return raw HTTP responses to application.
    - Hide rate-limit and auth errors.
