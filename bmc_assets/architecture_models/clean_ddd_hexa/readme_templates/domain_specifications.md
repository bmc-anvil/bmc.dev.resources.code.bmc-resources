# Domain Specifications

Contains:

- Reusable predicates/rules combinable (and/or/not).

Purpose:

- Encapsulate complex validation and selection logic.

Guidance:

- Do:
    - Keep specifications pure and testable.
    - Combine for expressive rule sets.
- Don’t:
    - Perform side effects or IO.
    - Depend on persistence entities.
