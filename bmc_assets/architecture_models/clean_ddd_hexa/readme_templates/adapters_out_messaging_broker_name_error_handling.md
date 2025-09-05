# Messaging Error Handling (Outbound)

Contains:

- Retry/backoff strategies, DLQ routing for publish failures.

Purpose:

- Provide resilient, observable publishing.

Guidance:

- Do:
    - Classify errors as transient vs permanent.
    - Surface failures with actionable logs/metrics.
- Don’t:
    - Infinite retry without safeguards.
    - Hide failures behind generic messages.
