# Outbound Messaging

Contains:

- Broker-specific producers with error handling and mappers.

Purpose:

- Publish events/commands to external brokers.

Guidance:

- Do:
    - Ensure idempotent publishing where required.
    - Version messages and use schemas when possible.
- Don’t:
    - Leak broker details through ports.
    - Ignore delivery failures without DLQ/alerts.
