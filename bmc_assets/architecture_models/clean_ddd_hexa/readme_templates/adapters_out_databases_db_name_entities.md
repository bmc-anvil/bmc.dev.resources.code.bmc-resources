# ORM Entities

Contains:

- Relational entity classes and mappings.

Purpose:

- Model persistence schema for relational databases.

Guidance:

- Do:
    - Keep entities persistence-focused.
    - Use mappers to convert to/from domain.
- Don’t:
    - Expose entities to application/domain layers.
    - Implement domain invariants here.
