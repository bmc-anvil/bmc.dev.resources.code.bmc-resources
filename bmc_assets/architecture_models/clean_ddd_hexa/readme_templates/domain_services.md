# Domain Services

Contains:

- Stateless services implementing domain logic that spans entities/aggregates.

Purpose:

- Express behaviors not naturally belonging to a single entity/value object.

Guidance:

- Do:
    - Keep stateless where possible and deterministic.
    - Accept domain types and return domain results.
- Don’t:
    - Perform IO; rely on ports via the application layer.
    - Accumulate state across calls.
