# Application Mappers

Contains:

- Mappings between application models and domain types.

Purpose:

- Centralize mechanical conversions to keep handlers clean.

Guidance:

- Do:
    - Keep mappings deterministic and side effect free.
    - Reuse mapping utilities consistently.
- Don’t:
    - Embed business rules.
    - Depend on transport or persistence models.
