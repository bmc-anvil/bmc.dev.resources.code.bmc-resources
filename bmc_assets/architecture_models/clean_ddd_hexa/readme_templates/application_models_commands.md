# Command Models

Contains:

- Models expressing state-changing intent.

Purpose:

- Capture inputs required to execute write operations.

Guidance:

- Do:
    - Validate required fields and invariants relevant to the application layer.
    - Keep immutable where possible.
- Don’t:
    - Contain domain or persistence logic.
    - Encode HTTP/messaging specifics.
