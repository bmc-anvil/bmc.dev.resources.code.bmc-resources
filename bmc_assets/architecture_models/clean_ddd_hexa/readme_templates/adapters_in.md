# Inbound Adapters

Contains:

- REST controllers/resources, inbound messaging listeners, DTOs, mappers, error handling.

Purpose:

- Receive external input and invoke input ports.

Guidance:

- Do:
    - Validate requests and map to application models.
    - Convert exceptions to transport-appropriate responses.
- Don’t:
    - Bypass the application layer to manipulate the domain directly.
    - Leak transport-specific types beyond adapters.
