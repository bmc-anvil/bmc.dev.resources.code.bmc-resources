# Use Cases — Commands

Contains:

- Command handlers implementing input ports for state-changing operations.
- Orchestration logic that coordinates domain actions and calls output ports.
- Transaction boundaries, domain event publication triggers.

Purpose:

- Execute write-intent workflows (create/update/delete), enforcing application-level rules and delegating business invariants to the domain.

Guidance:

- Do:
    - Keep handlers cohesive: validate inputs, orchestrate domain methods, call output ports, emit events.
    - Enforce application-level policies (authorization, idempotency) before invoking domain logic.
    - Use application mappers for conversions; return application/domain outputs as needed.
    - One file per intent / action.
- Don’t:
    - Implement persistence logic or directly manipulate DB clients.
    - Embed business rules that belong to entities/aggregates/services.
    - Depend on transport or persistence models (REST DTOs, ORM entities).
    - One file grouping multiple actions.
