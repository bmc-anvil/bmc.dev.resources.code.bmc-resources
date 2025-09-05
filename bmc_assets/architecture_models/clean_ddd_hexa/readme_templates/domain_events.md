# Domain Events

Contains:

- Domain event types and related policies/handlers (if in-domain).

Purpose:

- Convey meaningful business occurrences within the domain.

Guidance:

- Do:
    - Name events in past tense (e.g., OrderPlaced).
    - Keep payload minimal and domain-focused.
- Don’t:
    - Encode transport/broker metadata here.
    - Use events for trivial state changes without business meaning.
