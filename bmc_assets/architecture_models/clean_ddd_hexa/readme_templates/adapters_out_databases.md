# Database Adapters (Outbound)

Contains:

- Per-database implementations with config, error handling, documents/entities, mappers.

Purpose:

- Implement repository ports using specific vendor technology.

Guidance:

- Do:
    - Keep ORM/driver code here.
    - Translate persistence exceptions to adapter/application exceptions.
- Don’t:
    - Use domain entities as persistence models.
    - Perform business logic in repositories.
