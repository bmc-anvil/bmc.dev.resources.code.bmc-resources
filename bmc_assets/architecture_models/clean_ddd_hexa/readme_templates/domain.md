# Domain Layer

Contains:

- Core business abstractions: model, services, factories, events, specifications, exceptions.

Purpose:

- Encapsulate business rules and invariants independent of frameworks and I/O.

Guidance:

- Do:
    - Keep the domain pure (no HTTP, DB, messaging, logging frameworks).
    - Enforce invariants within aggregates and entities.
    - Use value objects for concepts defined by attributes.
- Don’t:
    - Depend on adapter or application layers.
    - Introduce persistence or transport annotations.
    - Expose domain internals directly to outside layers.
