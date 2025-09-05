# Messaging Adapter - Broker (Outbound)

Contains:

- Producer clients and broker-specific configuration.

Purpose:

- Encapsulate broker integration for publishing.

Guidance:

- Do:
    - Batch/async publish where appropriate.
    - Add metrics and tracing for publish paths.
- Don’t:
    - Hardcode topics/queues; make configurable.
    - Couple tightly to domain types.
