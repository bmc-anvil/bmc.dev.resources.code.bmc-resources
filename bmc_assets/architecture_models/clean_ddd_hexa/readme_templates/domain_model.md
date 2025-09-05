# Domain Model

Contains:

- Aggregates, entities, value objects composing the ubiquitous language.

Purpose:

- Represent business concepts and behavior.

Guidance:

- Do:
    - Favor rich models with behavior close to data.
    - Keep invariants within aggregate boundaries.
    - Prefer immutability for value objects.
- Don’t:
    - Add getters/setters only; avoid anemic models.
    - Reference frameworks or adapter types.
    - Cross aggregate boundaries with direct references (use IDs/services).
