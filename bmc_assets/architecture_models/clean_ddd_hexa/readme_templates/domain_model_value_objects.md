# Value Objects

Contains:

- Immutable types defined by attributes (no identity).

Purpose:

- Express small, composable domain concepts.

Guidance:

- Do:
    - Validate in constructors/factories.
    - Make them immutable and side effect free.
- Don’t:
    - Add mutable state or lifecycle.
    - Embed infrastructure concerns.
