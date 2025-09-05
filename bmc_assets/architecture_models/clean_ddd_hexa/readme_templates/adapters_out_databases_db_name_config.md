# Database Config

Contains:

- Data sources/clients, connection pools, migration wiring.

Purpose:

- Centralize persistence configuration.

Guidance:

- Do:
    - Externalize config and tune pools properly.
    - Secure secrets via environment/secret stores.
- Don’t:
    - Intermix business rules here.
    - Duplicate configuration across modules.
