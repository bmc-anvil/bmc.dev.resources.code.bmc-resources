# Messaging Error Handling (Inbound)

Contains:

- Retry, backoff, dead-letter strategies and handlers.

Purpose:

- Ensure resilient consumption and traceability.

Guidance:

- Do:
    - Instrument with metrics and logs.
    - Handle poison messages deterministically.
- Don’t:
    - Infinite-retry without DLQ.
    - Hide failures without alerts.
