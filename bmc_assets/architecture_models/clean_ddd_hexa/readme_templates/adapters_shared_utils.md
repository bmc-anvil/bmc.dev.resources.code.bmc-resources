# Adapter Utilities

Contains:

- Helper classes/methods for adapters (serialization, headers, etc.).

Purpose:

- DRY utilities strictly for adapter concerns.

Guidance:

- Do:
    - Keep small and cohesive.
    - Add tests; avoid global state.
    - Think about abstracting utilities from here into their own library to be reused on more than one project (string, time, parsing, etc.)
- Don’t:
    - Add business logic.
    - Depend on application/domain packages.
