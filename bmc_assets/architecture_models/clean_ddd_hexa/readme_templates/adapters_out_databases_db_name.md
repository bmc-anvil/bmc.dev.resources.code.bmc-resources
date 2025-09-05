# Database Adapter - Implementation

Contains:

- Technology-specific code for a particular database.

Purpose:

- Encapsulate client/ORM and schema details.

Guidance:

- Do:
    - Document migrations and schema ownership.
    - Provide integration tests with containers/mocks.
- Don’t:
    - Hardcode credentials or URLs.
    - Mix multiple database responsibilities.
