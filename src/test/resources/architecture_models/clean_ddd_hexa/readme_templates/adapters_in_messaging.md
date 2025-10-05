# Inbound Messaging

Contains:

- broker_name folders with config, dtos, error_handling, listeners, mappers.

Purpose:

- Consume messages and invoke appropriate input ports.

Guidance:

- Do:
    - Validate and dead-letter on poisoned messages.
    - Make consumers idempotent.
- Don’t:
    - Depend on application internals beyond input ports.
    - Hide retry policies inside business logic.
