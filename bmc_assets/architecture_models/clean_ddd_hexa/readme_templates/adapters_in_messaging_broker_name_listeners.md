# Message Listeners

Contains:

- Consumer/listener classes.

Purpose:

- Entry point for messages; validate, map, and call input ports.

Guidance:

- Do:
    - Keep listeners thin and idempotent.
    - Handle ordering/duplication as needed.
- Don’t:
    - Implement business logic.
    - Manage transactions directly (delegate appropriately).
