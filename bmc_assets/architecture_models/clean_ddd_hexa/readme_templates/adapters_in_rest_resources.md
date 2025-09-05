# REST Resources / Controllers

Contains:

- Endpoint/controller classes.

Purpose:

- Handle HTTP routing, validation, and delegation to input ports.

Guidance:

- Do:
    - Keep methods short: validate, map, call, map back.
    - Enforce idempotency/HTTP semantics.
- Don’t:
    - Perform transactions or persistence logic.
    - Embed complex mapping inline.
