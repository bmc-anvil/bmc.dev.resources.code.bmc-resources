# Domain Driven Design / Clean Architecture / Hexagonal Architecture (Ports and Adapters)

## What this is

This is a standardized structure to use as a starting point on new projects that want a DDD / Hexagonal / Clean architecture blend.

I have not encountered many folder structures out there that would match what I wanted to achieve, so I created this one.

This was done after reading the classics and a few more modern and to-the-point books like **Davi Vieira**'s _Designing Hexagonal Architecture with
Java: [...]_.

If you are familiar with their work, you'll find a lot of common points in the following structure.

The main goal can be found in **Uncle Bob**'s _Clean Architecture: [...]_ pages, about software easy to understand, maintain, change and extend over
time.
<br>Evolution costs and maintenance costs are at the core of the goal.

This is one of many approaches to clean architecture / DDD / hexagonal that fit my own needs and will evolve as the project evolves.

In the below folder structure you will see covered many possibilities for any application.

## What this is NOT

This structure is by no means a strict approach to any particular strategy or book by E. Evans, R. C. Martin, A. Cockburn, etc... but a blend that
suits my needs the best.

If you are looking for a pure approach to any one of the above strategies, this is not it.

> This is not the best fit for every service in every problem space, using this structure for everything is a mistake.

### Example on "small" deviations:

In this structure `adapters` are grouped by technology and not by context or aggregate, diluting a little the ubiquitous language as described
by Evans.
<br> Have I gone that way, the structure would have gotten out of hands on most projects, so I grouped them inside their corresponding technology
boxes.

You may notice there is no `persistence` in the domain as DDD would suggest, and instead you'll find persistence abstractions on the application layer
and implementation on the adapter layer, closer to ports and adapters approach.
<br> This may generate a bit more anemic domain; I'll see how this evolves
when / if a given domain is shared.

CQS (CQRS) lingo is used, though not strictly adopted as proper full data mutation separation, i.e., true full segregated W/R approach.

### Example on naming blend:

DDD does not have an `adapter` layer, but interface (UI) / infrastructure layer. The `adapter` comes from **_ports and adapters_** (hexagonal
architecture).

`Domain` in DDD is inside the `Core` in HexaArch and is referred to as `entities` within business rules in CleanArch.

# How to modify what "_is not_" to what "_it is_" you want to be

If you read the books or have an AI summarize the books for you, you will be able to determine where I deviated one way or the other.

The below structure and the creation script are made on purpose in a way that you can copy-paste it into an AI prompt and have it modified to your
best liked approach.
<br> It only takes a few seconds, and you already have a starting point in the below structure.

### If you are curious how this structure ranks regarding adherence to industry practices, here is an AI-generated (GPT-5) table analyzing my structure to the different approaches.

| Layer/Folder                                     | Purpose (as structured)                                                                                                  | DDD Alignment | Clean Architecture Alignment | Hexagonal Alignment (Ports & Adapters) | Score (1-10) | Comment (only if score ≤ 7)                                                                                    |
|--------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|---------------|------------------------------|----------------------------------------|-------------:|----------------------------------------------------------------------------------------------------------------|
| domain                                           | Core business model: aggregates, entities, value objects, domain services, specifications, factories, events, exceptions | Strong        | Strong                       | Neutral                                |           10 |                                                                                                                |
| application                                      | Use cases, ports (in/out), models (commands/queries), mappers                                                            | Strong        | Strong                       | Strong                                 |            9 |                                                                                                                |
| adapters/in                                      | Inbound adapters (REST controllers, messaging listeners) with dtos, mappers, error handling                              | Good          | Strong                       | Strong                                 |            9 |                                                                                                                |
| adapters/out                                     | Outbound adapters (DB, REST clients, messaging producers) with config, entities/documents, mappers, error handling       | Good          | Strong                       | Strong                                 |            9 |                                                                                                                |
| adapters/shared                                  | Cross-cutting in adapter tier: config, error handling, constants, utils, events                                          | Mixed         | Cautious fit                 | Neutral                                |            6 | Constrain to adapter-only concerns to avoid becoming a dumping ground; prevent imports from core layers.       |
| application/mappers                              | Mapping between layers                                                                                                   | Good          | Good                         | Neutral                                |            7 | Keep mapping mechanical; avoid embedding business rules; document directionality between transport/app/domain. |
| application/models (commands/queries)            | CQRS separation                                                                                                          | Strong        | Strong                       | Neutral                                |            8 |                                                                                                                |
| application/ports/in                             | Use case interfaces                                                                                                      | Strong        | Strong                       | Strong                                 |            9 |                                                                                                                |
| application/ports/out/repositories               | Outbound dependencies (repos, gateways)                                                                                  | Strong        | Strong                       | Strong                                 |            9 |                                                                                                                |
| adapters/out/databases/.../entities              | ORM entities                                                                                                             | Good          | Strong                       | Strong                                 |            9 |                                                                                                                |
| adapters/in/rest/...                             | Controllers/resources, request/response DTOs, error handling                                                             | Strong        | Strong                       | Strong                                 |            9 |                                                                                                                |
| adapters/in/messaging and adapters/out/messaging | Consumers/producers, config, error handling                                                                              | Strong        | Strong                       | Strong                                 |            9 |                                                                                                                |

> Note:
> <br> Adapter Shared folder:
>
> It is a risk that carelessness transforms it into a dumping ground, true.
> <br>The reason it is there is to avoid repetition in shared concerns or configs that cannot go anywhere else, i.e., mapstruct general configuration.
>
> Given the existence of the rest of the folders, if many things get dumped in the shared folders, we can use the experience to adapt and enhance the
> structure.

# Why

I chose this approach as it was the one offering the most long-term extensibility with the lower effort for my needs, with my knowledge... at this
time...

I've seen at many workplaces how choosing any strategy that fits the company can get out of hands if there is no automated way to replicate it project
over project. <br>
Generating this structure via a script that may be embedded in some build tool guarantees alignment and consistency across the board. It also
unburdens developers / architects / devops / etc. to manually create and replicate a given way of coding.

I've also seen how a free-for-all approach where each dev chooses whatever strategy they consider ok wreaks havoc before long even on small teams.

Devil's advocate on simplicity...

It may be daunting encountering this structure at first sight. However, once you get your bearings and every (applicable) project is configured in
this same way, the complexity (and time / effort...) of moving from one service to the next drops to zero.

## Folder structure

### Tree View

```
src/
├── main/
│   └── java/
│       └── com/
│           └── example.../
│               └── appname/
│                   ├── adapters/                           # Adapter Layer: Interfaces with external systems (Controllers, API Clients, DBs, Storage, Brokers, etc.)
│                   │   ├── in/                             # Adapter Input: Accept data into the application
│                   │   │   ├── rest/                       # Adapter Input REST: Handle REST request
│                   │   │   │   ├── dtos/                   # Adapter Input REST DTOs: Data Transfer Objects for communication
│                   │   │   │   │   ├── common/             # Adapter Input REST DTOs common: Represent common data to build more than one DTO (Id, date-time, etc.)
│                   │   │   │   │   ├── requests/           # Adapter Input REST DTOs Request: Represent incoming user input
│                   │   │   │   │   └── responses/          # Adapter Input REST DTOs Response: Represent outgoing data
│                   │   │   │   ├── error_handling/         # Adapter Input REST Error Handling: error handling logic, can be framework centered or not (Quarkus, spring, manual, etc.)
│                   │   │   │   ├── mappers/                # Adapter Input REST Mappers: from / to application layer
│                   │   │   │   └── resources/              # Adapter Input REST Controllers / Resources
│                   │   │   │
│                   │   │   └── messaging/                  # Adapter Input Message Brokers / Queues / etc.
│                   │   │       └── broker_name/            # Adapter Input broker_name: implementation name (kafka, rabbit, etc.)
│                   │   │           ├── config/             # Adapter Input broker_name config: config for the implementation
│                   │   │           ├── dtos/               # Adapter Input broker_name DTOs: Data Transfer Objects for communication
│                   │   │           ├── error_handling/     # Adapter Input broker_name Error Handling: error handling logic, can be framework centered or not (Quarkus, spring, manual, etc.)
│                   │   │           ├── listeners/          # Adapter Input broker_name Listeners: message consumers
│                   │   │           └── mappers/            # Adapter Input broker_name Mappers: from / to application layer
│                   │   │
│                   │   └── out/                            # Adapter Output: Implement external integrations
│                   │       ├── databases/                  # Adapter Output Implementation for database repositories
│                   │       │   └── db_name/                # Adapter Output db_name: implementation name (mysql, postgresql, dynamobd, mongodb, etc.)
│                   │       │       ├── config/             # Adapter Output db_name config: config for the implementation
│                   │       │       ├── error_handling/     # Adapter Output db_name Error Handling: error handling logic, can be framework centered or not (Quarkus, spring, manual, etc.)
│                   │       │       ├── documents/          # Adapter Output db_name documents: NoSQL Documents
│                   │       │       ├── entities/           # Adapter Output db_name entities: hibernate entities
│                   │       │       └── mappers/            # Adapter Output db_name Mappers from / to application layer
│                   │       │
│                   │       ├── messaging/                  # Adapter Output Message Brokers / Queues / etc.
│                   │       │   └── broker_name/            # Adapter Output broker_name: implementation name (Kafka, RabbitMQ, etc.)
│                   │       │       ├── error_handling/     # Adapter Output broker_name Error Handling: error handling logic, can be framework centered or not (Quarkus, spring, manual, etc.)
│                   │       │       ├── mappers/            # Adapter Output broker_name Mappers: from / to application layer
│                   │       │       └── producers/          # Adapter Output broker_name producers: message producers
│                   │       │           └── dtos/           # Adapter Output broker_name DTOs: Data Transfer Objects for communication
│                   │       │
│                   │       ├── rest/                       # Adapter Output REST: Outgoing Rest calls
│                   │       │   └── clients/                # Adapter Output REST Clients: API clients to other systems
│                   │       │       ├── config/             # Adapter Output REST Clients config: config for the implementation
│                   │       │       ├── dtos/               # Adapter Output REST Clients DTOs: Data Transfer Objects for communication
│                   │       │       ├── error_handling/     # Adapter Output REST Clients Error Handling: error handling logic, can be framework centered or not (Quarkus, spring, manual, etc.)
│                   │       │       └── mappers/            # Adapter Output REST Mappers from / to application layer
│                   │       │
│                   │       └── shared/                     # Adapter Shared Layer: Cross-cutting reusable components
│                   │           ├── config/                 # Adapter Shared Config: share configuration (common mapper config, mapstruct, etc.)
│                   │           ├── events/                 # Adapter Shared Event System: Publish/Subscribe events
│                   │           ├── error_handling/         # Adapter Shared Error Handling: Global and reusable exceptions
│                   │           ├── constants/              # Adapter Shared Constants: Application-wide static values
│                   │           └── utils/                  # Adapter Shared Utils: Helper classes and shared logic
│                   │
│                   ├── application/                        # Application Layer: Orchestrates use cases and workflows (application Logic, Core Use Cases)
│                   │   ├── mappers/                        # Application Mappers: Translate between application and domain objects
│                   │   │
│                   │   ├── models/                         # Application Models:
│                   │   │   ├── commands/                   # Application Models commands: DTOS equivalent in the application layer (write logic ones)
│                   │   │   └── queries/                    # Application Models queries: DTOS equivalent in the application layer (read logic ones)
│                   │   │
│                   │   ├── ports/                          # Application Ports: Define boundaries for use cases
│                   │   │   ├── in/                         # Application Ports in: Interfaces for incoming requests (Use Cases)
│                   │   │   └── out/                        # Application Ports out: Interfaces for external resources/services (repositories, API gateways)
│                   │   │       └── repositories/           # Application Repository interfaces: db interfaces
│                   │   │
│                   │   └── use_cases/                      # Application Use Case Implementations: Application logic for workflow
│                   │       ├── commands/                   # Application Command Handlers: Handle state-changing (write logic) operations (can be a handler folder in case commands themselves are different from requestDTOs)
│                   │       └── queries/                    # Application Query Handlers: Handle read-only operations
│                   │
│                   └── domain/                             # Domain Layer: Business Logic Core
│                       ├── events/                         # Domain Events: Track State Changes or occurrences in the domain
│                       ├── exceptions/                     # Domain Exceptions: Domain-specific errors or rule violations
│                       ├── factories/                      # Domain Factories: Centralized creation logic for domain objects (Instantiate Aggregates / Entities)
│                       │
│                       ├── model/                          # Domain Models (Aggregates, Entities, Value Objects)
│                       │   ├── aggregates/                 # Domain Aggregates: Root entities managing groups of entities
│                       │   ├── entities/                   # Domain Entities: Non-root Entities (Shared or Included in Aggregates) / Domain objects with unique identity
│                       │   └── valueobjects/               # Domain Value Objects: Immutable domain objects (no identity)
│                       │
│                       ├── services/                       # Domain Services: Express core business logic (Business Logic Across Aggregates / Entities)
│                       └── specifications/                 # Domain Specifications: Domain validations rules
│
└── README.md                                               # Main README file describing the project
```

## What's next

Use the [generator script](../architecture_model_generator.sh) and the [configuration file](../model_config.env) to create the above folder structure.

## References

DDD:

- Eric Evans:
    - [Domain-Driven Design: Tackling Complexity in the Heart of Software](https://www.amazon.com/Domain-Driven-Design-Tackling-Complexity-Software-ebook/dp/B00794TAUG)
- Vaughn Vernon:
    - [Implementing Domain-Driven Design](https://www.amazon.com/Implementing-Domain-Driven-Design-Vaughn-Vernon-ebook/dp/B00BCLEBN8/)

Clean Architecture:

- Robert C. Martin (Uncle Bob):
    - [Clean  Architecture: [...]](https://www.pearson.com/en-us/subject-catalog/p/clean-architecture-a-craftsmans-guide-to-software-structure-and-design/P200000009528/9780134494326)

Hexagonal Architecture:

- Alistair CockBurn:
    - [Hexagonal Architecture Explained: [...]](https://www.amazon.com/Hexagonal-Architecture-Explained-architecture-simplifies-ebook/dp/B0F5F7YRWW)
- Davi Vieira:
    - [Designing Hexagonal Architecture with Java: [...]](https://www.packtpub.com/en-us/product/designing-hexagonal-architecture-with-java-9781801816489)

CQS / CQRS Architecture:

- Command-Query Separation (CQS):
    - [wikipedia article on CQS](https://en.wikipedia.org/wiki/Command%E2%80%93query_separation)
- Command-Query Responsibility Segregation (CQRS)
    - [wikipedia article on CQRS](https://en.wikipedia.org/wiki/Command_Query_Responsibility_Segregation)
    - [Martin Fowler's article on CQRS](https://martinfowler.com/bliki/CQRS.html)
    - [Greg Young's article on CQRS](https://cqrs.wordpress.com/wp-content/uploads/2010/11/cqrs_documents.pdf)
