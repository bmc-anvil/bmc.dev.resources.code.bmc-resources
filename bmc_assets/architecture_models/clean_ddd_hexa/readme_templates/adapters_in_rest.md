# REST Inbound

Contains:

- dtos (common/requests/responses), error_handling, mappers, resources.

Purpose:

- Expose HTTP endpoints and translate to application interactions.

Guidance:

- Do:
    - Keep controllers thin and delegating.
    - Separate request and response DTOs.
- Don’t:
    - Place domain logic in controllers.
    - Return domain entities directly.
