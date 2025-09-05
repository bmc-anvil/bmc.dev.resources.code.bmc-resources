# REST Client Mappers

Contains:

- Conversions between external DTOs and domain/app models.

Purpose:

- Isolate provider-specific structures.

Guidance:

- Do:
    - Handle partial/optional fields robustly.
    - Test with real provider samples.
- Don’t:
    - Embed business decisions or side effects.
    - Depend on HTTP client classes.
