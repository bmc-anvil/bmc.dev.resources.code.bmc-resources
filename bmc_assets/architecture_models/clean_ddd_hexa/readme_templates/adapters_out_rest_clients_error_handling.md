# REST Client Error Handling

Contains:

- Translation of HTTP failures to application exceptions and retryable categories.

Purpose:

- Provide consistent error semantics to application.

Guidance:

- Do:
    - Parse error bodies for actionable messages.
    - Classify retryable vs non-retryable errors.
- Don’t:
    - Swallow rate-limit/auth errors.
    - Throw raw HTTP exceptions to application.
