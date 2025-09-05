# Persistence Mappers

Contains:

- Conversions between application models and persistence entities/documents.

Purpose:

- Isolate persistence-specific shapes from the application.

Guidance:

- Do:
    - Keep mapping deterministic and reversible where possible.
    - Handle nullability and defaults carefully.
- Don’t:
    - Add IO or transactions.
    - Embed business rules.
