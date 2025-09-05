# Inbound Message DTOs

Contains:

- Message payload schemas/classes.

Purpose:

- Represent consumed messages before mapping to application models.

Guidance:

- Do:
    - Support versioning and backward compatibility.
    - Validate minimally before mapping.
- Don’t:
    - Embed domain rules.
    - Depend on application or domain types.
