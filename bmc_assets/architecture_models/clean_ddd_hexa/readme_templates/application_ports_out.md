# Output Ports

Contains:

- Interfaces for external services, repositories, and gateways.

Purpose:

- Abstract external dependencies required by use cases.

Guidance:

- Do:
    - Return domain/application models.
    - Capture behavior, not technology details.
- Don’t:
    - Depend on HTTP clients, entities, or broker classes.
    - Leak retry/timeout concerns into interfaces (configure in adapters).
