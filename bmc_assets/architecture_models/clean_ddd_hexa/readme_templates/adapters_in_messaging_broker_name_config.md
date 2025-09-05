# Broker Config (Inbound)

Contains:

- Consumer groups, topics/queues, client properties.

Purpose:

- Centralize consumer configuration.

Guidance:

- Do:
    - Externalize configs and document defaults.
    - Secure credentials/secrets properly.
- Don’t:
    - Hardcode environment-specific values.
    - Mix business logic into configuration.
