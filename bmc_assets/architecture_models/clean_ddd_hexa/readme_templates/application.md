# Application Layer

Contains:

- Ports (in/out), use cases (commands/queries), models, mappers.

Purpose:

- Orchestrate use cases, coordinate domain operations, and manage transactions/publishing.

Guidance:

- Do:
    - Implement use cases via input ports; call output ports.
    - Keep logic application-centric, delegating business rules to domain.
- Don’t:
    - Embed framework/persistence logic.
    - Expose domain internals directly to adapters.
