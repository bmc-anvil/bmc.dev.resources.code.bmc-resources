# Messaging Mappers (Outbound)

Contains:

- Conversions from domain/app models to broker DTOs.

Purpose:

- Shape messages for broker compatibility and schema evolution.

Guidance:

- Do:
    - Include event version and timestamps when relevant.
    - Keep payload minimal and meaningful.
- Don’t:
    - Publish raw domain entities.
    - Encode sensitive data without redaction/encryption.
