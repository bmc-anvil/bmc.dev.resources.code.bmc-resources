# Aggregates

Contains:

- Aggregate root classes and related logic.

Purpose:

- Control transactional invariants and consistency boundaries.

Guidance:

- Do:
    - Keep aggregates small and cohesive.
    - Emit domain events for meaningful changes.
    - Validate state changes through methods, not public fields.
- Don’t:
    - Expose mutable internal collections directly.
    - Allow external code to bypass invariants.
    - Couple root to IO/persistence concerns.
