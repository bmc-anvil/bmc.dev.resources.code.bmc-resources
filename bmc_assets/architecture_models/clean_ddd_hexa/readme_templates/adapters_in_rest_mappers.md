# REST Mappers (Inbound)

Contains:

- Mappings between REST DTOs and application models.

Purpose:

- Isolate transport-to-application conversion.

Guidance:

- Do:
    - Normalize/validate fields as needed.
    - Keep mapping deterministic and tested.
- Don’t:
    - Implement business decisions.
    - Depend on domain persistence models.
