# Response DTOs

Contains:

- Outgoing payload shapes for REST endpoints.

Purpose:

- Return results with appropriate metadata (e.g., pagination, links).

Guidance:

- Do:
    - Keep consistent error and data envelopes.
    - Avoid overexposing internal structure.
- Don’t:
    - Return domain entities or persistence models.
    - Break backward compatibility without versioning.
