# Project Main Overview

Purpose:

- Contains all core layers of the application

Contains:

- adapters: Technology-specific implementation of I/O with the outside world (REST, messaging, DBs, external APIs).
- application: orchestrates use cases and coordinates domain interactions.
- domain: core business model and rules.

Guidance:

- Keep domain independent of frameworks and adapters.
- All dependencies flow inward (adapters -> application -> domain).
- Use cases expose input/output ports. Outbound ports express external needs.
