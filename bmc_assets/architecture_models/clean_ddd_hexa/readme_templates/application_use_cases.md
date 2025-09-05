# Use Cases

Contains:

- Command and query handlers implementing input ports.
- These are the application "services" / "orchestrators"

Purpose:

- Coordinate domain operations and call output ports; manage transactions and event publication.

Guidance:

- Do:
    - Keep handlers focused and cohesive.
    - One (1) file per use case. Example:
      - AddUserUseCase -> `execute(final UserCommand userCommand)`
      - UpdateUserUseCase -> `execute(final UserCommand userCommand)`
      - GetUserByIdUseCase -> `execute(final Id id)`
      - GetUserBySomeCriteriaUseCase -> `execute(final UserQuery userQuery)`
    - Validate application-level constraints and orchestrate domain calls.
- Don’t:
    - Implement persistence logic here.
    - Perform heavy mapping inline (use mappers).
    - Use generic use case files to implement multiple use cases. Example:
        - UserUseCase has:
            - `addUser()`, `updateUser()`, `findUserBy()`, `findAllUsers()`
