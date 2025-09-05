# DB Error Handling

Contains:

- Exception translation, retry policies, integrity mapping.

Purpose:

- Provide consistent failure semantics to application layer.

Guidance:

- Do:
    - Map constraint violations to meaningful errors.
    - Add observability for query failures and timeouts.
- Don’t:
    - Swallow critical errors silently.
    - Leak vendor-specific exceptions upward.
