# Inbound Messaging - Broker

Contains:

- Broker-specific inbound components i.e., kafka, rabbit, aws SQS, etc.

Purpose:

- Implement broker client details and message consumption.

Guidance:

- Do:
    - Keep broker implementations localized.
    - Map payloads early to app models.
- Don’t:
    - Cross-contaminate with other brokers’ code.
    - Leak broker metadata to the application layer.
