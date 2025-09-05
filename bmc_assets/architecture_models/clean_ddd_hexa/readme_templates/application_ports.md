# Application Ports

Contains:

- Input ports (use case interfaces) and output ports (external dependencies).

Purpose:

- Define stable contracts at application boundaries.

Guidance:

- Do:
    - Keep ports technology-agnostic.
    - Express operations in domain/application terms.
- Don’t:
    - Use transport or ORM types.
    - Leak framework annotations.
