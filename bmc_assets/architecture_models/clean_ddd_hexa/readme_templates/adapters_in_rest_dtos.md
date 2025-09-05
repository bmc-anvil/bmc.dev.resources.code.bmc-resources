# REST DTOs (Inbound)

Contains:

- Transport models used at the HTTP boundary.

Purpose:

- Define input/output payload shapes for REST endpoints.

Guidance:

- Do:
    - Version DTOs when breaking changes are needed.
    - Keep DTOs tailored to API needs (not domain).
- Don’t:
    - Reuse domain types as DTOs.
    - Mix persistence annotations.
