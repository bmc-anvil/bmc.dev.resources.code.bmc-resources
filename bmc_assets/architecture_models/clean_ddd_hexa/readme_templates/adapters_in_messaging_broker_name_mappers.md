# Messaging Mappers (Inbound)

Contains:

- Conversions from message DTOs to application models.

Purpose:

- Isolate broker payload specifics from the application layer.

Guidance:

- Do:
    - Normalize payloads and handle optional/legacy fields.
    - Test mapping against real sample messages.
- Don’t:
    - Encode business rules or side effects.
    - Depend on broker SDKs here.
