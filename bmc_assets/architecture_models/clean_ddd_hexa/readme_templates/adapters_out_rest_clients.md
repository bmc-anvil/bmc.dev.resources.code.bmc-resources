# REST Clients

Contains:

- Client interfaces/implementations for external APIs.
- Given the nature of some tech, interfaces that are auto-implemented live here too.

Purpose:

- Implement outbound HTTP calls and response handling.

Guidance:

- Do:
    - Use typed DTOs and structured errors.
    - Log request/response metadata (without secrets).
    - Create a client per context
- Don’t:
    - Share clients across unrelated domains tightly.
    - Hardcode URLs/credentials.
    - Reuse a single client for every call on disparate concerns.
