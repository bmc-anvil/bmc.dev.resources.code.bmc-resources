# Common DTO Components

Contains:

- Shared DTO fragments/types (IDs, timestamps, pagination).

Purpose:

- Avoid duplication across request/response DTOs.

Guidance:

- Do:
    - Keep focused on cross-cutting transport concerns.
    - Document reuse intent and stability.
- Don’t:
    - Accumulate domain logic here.
    - Over-generalize; prefer clarity.
