# Outbound Adapters

Contains:

- Databases, REST clients, messaging producers.

Purpose:

- Implement output ports against concrete technologies.

Guidance:

- Do:
    - Map persistence/client models to domain/app types.
    - Apply resilience patterns (timeouts, retries, circuit breakers).
- Don’t:
    - Return ORM/entities or raw responses to application.
    - Leak technology details through port interfaces.
