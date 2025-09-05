# REST Client DTOs

Contains:

- Request/response models for external APIs.

Purpose:

- Represent external contracts and map to application/domain.

Guidance:

- Do:
    - Keep models aligned to provider API.
    - Version when external contracts change.
- Don’t:
    - Reuse inbound API DTOs.
    - Bleed domain invariants into client models.
