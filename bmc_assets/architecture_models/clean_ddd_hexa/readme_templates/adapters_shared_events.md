# Adapter Events

Contains:

- Adapter-level events/notifications (non-domain).

Purpose:

- Signal adapter lifecycle/integration events.

Guidance:

- Do:
    - Keep separate from domain events.
    - Use for observability/integration state.
- Don’t:
    - Model business events here.
    - Create tight coupling with domain.
