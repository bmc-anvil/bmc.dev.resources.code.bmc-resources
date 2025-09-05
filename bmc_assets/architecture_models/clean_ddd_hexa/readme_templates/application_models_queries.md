# Query Models

Contains:

- Models for read-only operations.

Purpose:

- Express data retrieval intent, including pagination/sorting where needed.

Guidance:

- Do:
    - Keep focused on query criteria.
    - Support pagination/filters explicitly.
- Don’t:
    - Include side effect triggers.
    - Depend on database-specific constructs.
