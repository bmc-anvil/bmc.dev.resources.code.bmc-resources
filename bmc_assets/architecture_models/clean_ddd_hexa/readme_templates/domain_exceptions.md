# Domain Exceptions

Contains:

- Domain-specific exception types.

Purpose:

- Signal rule violations and invalid states.

Guidance:

- Do:
    - Use specific types with clear messages.
    - Keep messages developer-oriented; map for users in adapters.
- Don’t:
    - Depend on HTTP or DB exception hierarchies.
    - Leak stack traces beyond adapters.
