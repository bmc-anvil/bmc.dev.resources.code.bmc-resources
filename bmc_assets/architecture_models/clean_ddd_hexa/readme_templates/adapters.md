# Adapter Layer

Contains:

- Inbound (in), outbound (out), and shared adapter utilities.

Purpose:

- Translate between application/domain and external technologies.

Guidance:

- Do:
    - Implement ports and map between formats.
    - Handle framework concerns (validation, error mapping).
    - Technology-specific implementations.
- Don’t:
    - Implement domain business rules.
    - Depend inward (domain should not depend on adapters).
