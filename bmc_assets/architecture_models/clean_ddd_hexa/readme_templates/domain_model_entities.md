# Entities

Contains:

- Non-root entities managed by an aggregate.
- If there are no relevant aggregates for a given entity, those non-aggregate entities live here too.

Purpose:

- Model domain concepts with identity within aggregate boundaries.

Guidance:

- Do:
    - Keep identity stable and managed by the aggregate root.
    - Encapsulate behavior and validation.
- Don’t:
    - Manage transactions or repositories here.
    - Depend on external services.
