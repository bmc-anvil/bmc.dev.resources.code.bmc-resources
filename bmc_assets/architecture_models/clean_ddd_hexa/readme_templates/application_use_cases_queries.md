# Use Cases — Queries

Contains:

- Query handlers implementing input ports for read-only operations.
- Orchestration for fetching data via output ports and mapping to application models.
- Read-side policies such as pagination, filtering, sorting.

Purpose:

- Serve read intent without side effects, presenting data in application-friendly shapes.

Guidance:

- Do:
    - Keep handlers read-only and side effect free.
    - Delegate data access to repository/output ports; apply pagination/sorting consistently.
    - Use mappers to convert persistence/client models to application/domain projections.
    - One file per intent / action.
- Don’t:
    - Mix write operations or publish domain events.
    - Leak persistence entities or external DTOs beyond the adapter boundary.
    - Encode database-specific constructs in port contracts or handlers.
    - One file grouping multiple actions.
