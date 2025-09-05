# Shared Adapter Errors

Contains:

- Reusable adapter-layer exceptions and error models.

Purpose:

- Standardize error handling across adapters.

Guidance:

- Do:
    - Keep mapping to transport/db/broker errors consistent.
    - Include correlation/trace IDs where applicable.
- Don’t:
    - Depend on domain exception classes.
    - Leak stack traces beyond logs.
