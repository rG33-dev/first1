# Project Mistakes Log

This file records the mistakes that showed up in this project so far, why they were problems, and what the correct pattern should be.

## 1. Mixing an in-memory controller with a MongoDB controller

### What happened

The Mongo-backed controller was created by copying the in-memory controller and then modifying it only partially.

Because of that, the controller still contained:

- a local `Map`
- request/response types based on the controller class itself
- empty CRUD methods
- logic that did not call the service or repository

### Why this was wrong

A MongoDB-backed controller should not store data in memory. Once a repository exists, the controller should delegate persistence to the service layer.

The correct flow is:

`Controller -> Service -> Repository -> Database`

The broken flow was:

`Controller -> Local Map / null`

### Lesson

When copying an older version of a controller, remove the old storage pattern completely before adding the new one.

## 2. Storing controller objects instead of entity objects

### What happened

The controller used structures like:

- `Map<Long, JournalEntryController2>`
- `Map<Long, JournalEntryControllerWithoutMongo>`

### Why this was wrong

A controller is part of the HTTP layer. It is not domain data and should never be the thing being saved, returned as business data, or stored in a collection as the application model.

The thing that should be stored and returned is the entity or DTO, in this case `JournalEntry`.

### Lesson

Keep layers separate:

- controller = HTTP handling
- service = business logic
- repository = database access
- entity/model = actual data

## 3. Autowiring a service but not using it

### What happened

`JournalEntryService` was injected into the controller, but the controller methods still used the local `Map` or returned `null`.

### Why this was wrong

Injection alone does nothing. If the methods do not call the service, the service and repository are effectively dead code.

### Lesson

If a service is injected, controller methods should usually delegate real work to it.

## 4. Two controllers mapped to the same path

### What happened

Both journal controllers used the same base route:

- `@RequestMapping("/_journal")`

and overlapping endpoints.

### Why this was wrong

Spring tries to register request mappings at startup. If two controllers claim the same route pattern, the application can fail with ambiguous mapping errors or behave unpredictably.

### Lesson

Only one controller should own a route family unless you intentionally split endpoints in a non-overlapping way.

## 5. Using the controller class as `@RequestBody`

### What happened

The request body type in the controller was the controller class itself instead of `JournalEntry`.

### Why this was wrong

The request body should represent incoming data, not the object that handles HTTP requests.

Wrong idea:

- `@RequestBody JournalEntryController2`

Correct idea:

- `@RequestBody JournalEntry`

### Lesson

Request body types should be entities or DTOs, never controllers.

## 6. Using two `@RequestBody` parameters in one method

### What happened

The update method tried to take both:

- an `id` as `@RequestBody`
- an object as `@RequestBody`

### Why this was wrong

Spring MVC only supports one request body per request mapping method.

### Correct pattern

Use:

- `@PathVariable` for route identifiers
- `@RequestBody` for the JSON object

Example pattern:

`PUT /_journal/id/{id}` with `@PathVariable String id` and `@RequestBody JournalEntry journalEntry`

### Lesson

Split request data by source:

- URL path -> `@PathVariable`
- query string -> `@RequestParam`
- JSON body -> `@RequestBody`

## 7. Mismatched path variable names

### What happened

The route used:

- `id/{myId}`

but the method parameter was:

- `@PathVariable long id`

### Why this was wrong

Spring matches path variables by name. `myId` and `id` are different names.

### Correct options

Either:

- rename the route to `{id}`

or:

- use `@PathVariable("myId") long id`

### Lesson

Route variable names and method parameter bindings must match exactly unless explicitly mapped.

## 8. Empty service layer

### What happened

The service existed but had no CRUD methods.

### Why this was wrong

Without service methods, the controller had no clean way to delegate operations to the repository.

### Lesson

A service should provide the operations the controller needs, such as:

- `getAll`
- `getById`
- `saveEntry`
- `deleteById`

## 9. Repository package confusion and bean discovery risk

### What happened

Earlier in the project, service and repository packages were placed outside the main application package tree.

### Why this was wrong

`@SpringBootApplication` scans from its package downward. If services or repositories are outside that tree, Spring may not discover them.

### Lesson

Keep the app under one root package, for example:

- `com.example.first1`
- `com.example.first1.controller`
- `com.example.first1.services`
- `com.example.first1.services.JournalEntryRepo`
- `com.example.first1.Entity`

## 10. Incomplete Kotlin entity for Spring Data / Jackson

### What happened

The Kotlin entity was written as a class with private fields and partial getters.

### Why this was wrong

Spring Data MongoDB and JSON binding work much better when the entity is defined in a clear, serializable form. The old version had:

- missing accessors
- no clean constructor/data shape
- fields that were hard to bind

### Correct pattern

Using a Kotlin `data class` made the entity straightforward for persistence and request/response mapping.

### Lesson

For Kotlin models used by Spring, a `data class` is usually the cleanest option.

## 11. Missing `kotlin-reflect`

### What happened

The project compiled, but Spring Data Mongo failed at startup with:

- `NoClassDefFoundError: kotlin/reflect/full/KClasses`

### Why this was wrong

When Spring reflects over Kotlin classes, especially for data mapping, `kotlin-reflect` is often required.

### Lesson

If Kotlin entities are used with Spring Data, include `kotlin-reflect` unless you know a specific setup does not need it.

## 12. Wrong or risky Maven dependency choices

### What happened

The build used:

- `spring-boot-starter-webmvc`
- `spring-boot-starter-webmvc-test`

### Why this was wrong

The normal Spring Boot starters are:

- `spring-boot-starter-web`
- `spring-boot-starter-test`

Using unusual artifact names can cause dependency issues or simply be the wrong dependency choice for a standard Boot app.

### Lesson

Start from the standard Boot starters unless you have a very specific reason not to.

## 13. Kotlin target and Java version mismatch

### What happened

The project declared Java 17 but Kotlin was targeting JVM 1.8.

### Why this was wrong

That mismatch can create confusing behavior and is unnecessary when the whole project is already on Java 17.

### Lesson

Align Kotlin and Java target versions unless you intentionally need a lower target.

## 14. Keeping Kotlin files under `src/main/java`

### What happened

Kotlin source files were placed under:

- `src/main/java`

### Why this is not ideal

It can work, but Maven/Kotlin tooling may produce duplicate-source-root warnings or a less clear project structure.

### Better structure

- Java files in `src/main/java`
- Kotlin files in `src/main/kotlin`

### Lesson

Use standard source layout to reduce tool friction.

## 15. Returning `null` placeholders in controller methods

### What happened

Several controller methods returned `null`.

### Why this was wrong

Returning `null` from unfinished controller methods hides the real missing implementation and produces broken API behavior.

### Lesson

If a method is not implemented yet, either:

- implement it properly
- throw a clear exception temporarily
- or do not expose the endpoint yet

## 16. Copying code without replacing the architecture behind it

### What happened

Code was copied from one version of the app into another version with a different persistence model.

### Why this was wrong

The syntax changed only partially, but the architecture did not. That left old assumptions inside new code.

### Lesson

When copying code, check all of these explicitly:

- storage mechanism
- data types
- route mappings
- dependency usage
- package structure
- returned values

## 17. Main overall pattern behind the mistakes

Most of the bugs came from one root issue:

The code mixed layers together instead of keeping them separate.

That created these symptoms:

- controller acting like storage
- repository not being used
- service existing but doing nothing
- entity not clearly defined as the data model

## Recommended rule set for future changes

Before adding a new feature, verify:

1. What is the model or entity?
2. Which controller owns the route?
3. Which service method handles the business logic?
4. Which repository method handles persistence?
5. Is the request body a DTO/entity instead of a controller?
6. Are the route variables mapped correctly?
7. Is the code using the same package root as the application?

If those seven things are clear first, most of the current bugs do not happen.

## 18. Incomplete user controller implementation

### What happened

`UserEntryController.kt` stopped in the middle of an unfinished update method:

- it called `saveUserEntry` with the wrong arguments
- it passed a `User` object to a method that expected a username string
- it ended with `userInDb.se`

### Why this was wrong

That was a hard compilation failure, so the application could not even build.

### Lesson

Partially written endpoints should not stay in the active codebase. Finish them or remove them until they are ready.

## 19. User entity and repository used different id types

### What happened

The `User` entity declared an `Int?` id while the repository was `MongoRepository<User, ObjectId>`.

### Why this was wrong

The repository generic id type must match the entity id type. If they differ, CRUD operations and method signatures become inconsistent and error-prone.

### Lesson

Choose one id type and use it everywhere for that entity.

## 20. User service was missing `@Service`

### What happened

`UserEntryService` had no Spring stereotype annotation.

### Why this was wrong

Spring would not register it as a bean, which would break dependency injection into the controller at runtime.

### Lesson

If a class is meant to be injected as a service, mark it with `@Service`.

## 21. Journal update used `POST` instead of `PUT`

### What happened

The journal controller used `@PostMapping("id/{myid}")` for update logic.

### Why this was wrong

`POST` is generally for creation. Updates should use `PUT` or `PATCH`, especially when the resource id is already known in the route.

### Lesson

Match the HTTP method to the operation semantics.

## 22. Journal delete took the id from the request body

### What happened

The delete endpoint expected an `ObjectId` as `@RequestBody`.

### Why this was wrong

For resource deletion by identifier, the normal API shape is `DELETE /resource/{id}` with the id in the path. Putting the id in the body complicates the API and is inconsistent with the rest of the controller.

### Lesson

Use `@PathVariable` for resource identifiers.

## 23. Production controller contained a large commented archive

### What happened

`JournalEntryController2.java` contained a long commented historical version of the controller below the live code.

### Why this was wrong

It made the file noisy and increased the risk of reusing stale code accidentally.

### Lesson

Keep teaching notes and history in documentation files, not inside production classes.

## 24. Kotlin files were in the Java source tree

### What happened

Kotlin files were stored under `src/main/java`, and the build reported duplicate-source-root warnings.

### Why this was wrong

The app could still try to compile, but the source layout was non-standard and made the mixed Java/Kotlin build harder to maintain.

### Lesson

Use the conventional layout:

- `src/main/java`
- `src/main/kotlin`
- `src/test/java`
- `src/test/kotlin`
