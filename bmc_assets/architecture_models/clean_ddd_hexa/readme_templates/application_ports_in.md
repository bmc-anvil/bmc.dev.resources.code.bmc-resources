# Input Ports

Contains:

- Use case interfaces invoked by inbound adapters.

Purpose:

- Define application capabilities for commands/queries.

Guidance:

- Do:
    - Keep interfaces concise and intention-revealing.
    - Accept command/query models as parameters.
- Don’t:
    - Introduce adapter-specific types.
    - Mix read and write responsibilities arbitrarily.
