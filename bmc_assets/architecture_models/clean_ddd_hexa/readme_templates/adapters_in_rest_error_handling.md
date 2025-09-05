# REST Error Handling

Contains:

- Exception mappers/handlers and error payload models.

Purpose:

- Translate exceptions to stable, client-friendly HTTP responses.

Guidance:

- Do:
    - Use consistent error codes and correlation IDs.
    - Log with context and avoid sensitive data leaks.
- Don’t:
    - Reveal stack traces to clients.
    - Swallow exceptions without observability.
