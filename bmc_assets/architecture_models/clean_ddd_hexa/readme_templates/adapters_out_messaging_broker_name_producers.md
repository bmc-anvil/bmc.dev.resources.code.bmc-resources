# Message Producers

Contains:

- Producer implementations.

Purpose:

- Provide high-level APIs to publish events/commands.

Guidance:

- Do:
    - Support correlation/causation IDs.
    - Provide sync/async variants if needed.
- Don’t:
    - Embed business logic in producers.
    - Couple to specific use cases unnecessarily.
