# Request DTOs

Contains:

- Incoming payload shapes for REST endpoints.

Purpose:

- Capture user inputs for commands/queries.

Guidance:

- Do:
    - Use validation annotations for basic constraints.
    - Map to application models explicitly.
- Don’t:
    - Include server-generated fields.
    - Leak domain errors directly.
