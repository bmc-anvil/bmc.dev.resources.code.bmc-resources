# Repository Ports

Contains:

- Contracts for persistence interactions.

Purpose:

- Provide persistence-agnostic access to domain aggregates/entities.

Guidance:

- Do:
    - Design around aggregates and domain needs.
    - Return domain types; accept domain IDs/criteria.
- Don’t:
    - Accept/return ORM entities or DTOs.
    - Encode SQL/NoSQL specifics.
