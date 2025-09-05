# NoSQL Documents

Contains:

- Document models and collection/table mappings for document stores.

Purpose:

- Persist data in a document-oriented format.

Guidance:

- Do:
    - Keep documents focused on persistence needs.
    - Map to domain via mappers.
- Don’t:
    - Reuse domain models as documents.
    - Add business logic to documents.
