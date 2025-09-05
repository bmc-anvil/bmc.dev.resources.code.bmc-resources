# Domain Factories

Contains:

- Factories for complex aggregate/entity/value object creation.

Purpose:

- Centralize creation logic and enforce a valid initial state.

Guidance:

- Do:
    - Use descriptive factory methods.
    - Enforce invariants at creation time.
- Don’t:
    - Leak persistence or transport details.
    - Create ambiguous or partial invalid objects.
