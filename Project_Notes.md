# Basic Project Structure and About Info

Before starting this project, it is important to understand some basic Spring Boot concepts. Spring Boot helps us build backend applications in a structured way by separating responsibilities into different layers.

This project mainly shows three stages of building a REST API:

1. Without an external database like MongoDB
2. Integrating MongoDB
3. Using `ResponseEntity` for better HTTP responses

## Basic Spring Concepts Used in This Project

Spring Boot creates and connects application classes automatically using annotations.

- `@SpringBootApplication`: Starts the Spring Boot application
- `@RestController`: Marks a class as a REST API controller
- `@Service`: Marks a class as the service layer
- `@Document`: Marks a class as a MongoDB document
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Map HTTP requests to methods

## Main Structure of This Project

The current project follows this layered structure:

`Client -> Controller -> Service -> Repository -> MongoDB`

Each layer has a specific job:

- Controller: Receives HTTP requests and sends responses
- Service: Contains business logic and acts as a middle layer
- Repository: Communicates with the database
- Entity/Model: Represents the actual data

## Classes Present in This Project

### 1. `JournalApp`

This is the main entry point of the application.

- File: `src/main/java/com/example/first1/JournalApp.java`
- It contains `SpringApplication.run(...)`
- When the program starts, Spring Boot scans the package and creates all required beans like controllers, services, and repositories

### 2. `HealthCheck`

This is a simple controller used to check whether the application is running.

- File: `src/main/java/com/example/first1/controller/HealthCheck.kt`
- Endpoint: `GET /health`
- Response: `"Ok"`

Important note:

`HealthCheck` in this project does not verify whether the connection is secured. It only confirms that the application is up and responding.

### 3. `JournalEntry`

This class is the data model or entity of the project.

- File: `src/main/java/com/example/first1/Entity/JournalEntry.kt`
- It represents one journal record
- It contains fields like:
  - `id`
  - `journalId`
  - `journalTitle`
  - `journalContent`

Annotations used:

- `@Document(collection = "journal")`: Stores the object in the MongoDB `journal` collection
- `@Id`: Marks the primary key of the document

So this class is the object that travels through all layers:

`Request JSON -> Controller -> Service -> Repository -> MongoDB`

### 4. `JournalEntryController2`

This class handles all journal-related HTTP requests.

- File: `src/main/java/com/example/first1/controller/JournalEntryController2.java`
- Base URL: `/journal`

It exposes the following endpoints:

- `POST /journal` -> create a new journal entry
- `GET /journal` -> get all journal entries
- `GET /journal/id/{id}` -> get one journal entry by id
- `DELETE /journal/id/{id}` -> delete an entry
- `PUT /journal/id/{id}` -> update an entry

This controller does not directly talk to MongoDB. Instead, it calls the service layer.

### 5. `JournalEntryService`

This class contains the service layer logic.

- File: `src/main/java/com/example/first1/services/JournalEntryService.java`

Its job is to:

- receive calls from the controller
- process business logic
- call the repository layer

Methods in this service:

- `getAll()`
- `saveEntry(...)`
- `getById(...)`
- `deleteById(...)`

This keeps the controller clean and avoids writing database logic directly inside the controller.

### 6. `Repo`

This is the repository interface for MongoDB.

- File: `src/main/java/com/example/first1/services/JournalEntryRepo/Repo.java`
- It extends `MongoRepository<JournalEntry, String>`

Because it extends `MongoRepository`, Spring automatically provides common methods like:

- `findAll()`
- `findById(id)`
- `save(entry)`
- `deleteById(id)`

So we do not need to manually write basic CRUD queries.

### 7. `application.properties`

This file stores application configuration.

- File: `src/main/resources/application.properties`

Current MongoDB configuration:

- `spring.data.mongodb.host=localhost`
- `spring.data.mongodb.port=27017`
- `spring.data.mongodb.database=journaldb`

This means the application connects to a MongoDB server running on the local machine and uses the database named `journaldb`.

## Case 1: Without External Database

This is the beginner version of a Spring Boot REST API.

In this case, data is stored in memory using Java collections like:

- `List`
- `Map`

Possible structure:

`Client -> Controller -> Service -> In-memory Storage`

or in a very basic project:

`Client -> Controller -> In-memory Storage`

### How It Works

1. The client sends a request
2. The controller receives the request
3. The controller or service stores the data in a local collection
4. Data is returned when needed

### Example

If the user sends:

`POST /journal`

then the request body is converted into a `JournalEntry` object and stored in memory.

### Limitation

The biggest drawback is that data is temporary.

- If the application stops, data is lost
- If the server restarts, data is lost

### Connection with Current Project

In this case:

- `JournalEntry` can still be used as the model class
- `JournalEntryController2` can still handle API requests
- `JournalEntryService` can still exist
- `Repo` is not required
- MongoDB settings in `application.properties` are not required

This is the first stage of learning because it helps understand REST APIs before adding a real database.

## Case 2: Integrating MongoDB

This is the current structure used in this project.

Once MongoDB is added, the architecture becomes more structured:

`Client -> Controller -> Service -> Repository -> MongoDB`

### Flow of Data

1. The client sends an HTTP request
2. `JournalEntryController2` receives it
3. The controller calls `JournalEntryService`
4. The service calls `Repo`
5. `Repo` performs the database operation in MongoDB
6. The result is returned back through service and controller to the client

### Example for Create Operation

For:

`POST /journal`

the flow is:

1. Request body JSON is converted to `JournalEntry`
2. Controller method `createEntry(...)` receives it
3. Controller calls `journalEntryService.saveEntry(...)`
4. Service calls `journalEntryRepo.save(...)`
5. MongoDB stores the document in the `journal` collection
6. Saved object is returned to the client

### Example for Read Operation

For:

`GET /journal`

the flow is:

1. Request reaches controller
2. Controller calls `journalEntryService.getAll()`
3. Service calls repository `findAll()`
4. Repository fetches all documents from MongoDB
5. Data returns as a list of `JournalEntry`

### Example for Update Operation

For:

`PUT /journal/id/{id}`

the flow is:

1. ID comes from the URL using `@PathVariable`
2. New data comes from JSON using `@RequestBody`
3. Controller sets the ID on the object
4. Service calls repository `save(...)`
5. MongoDB updates the existing document if the ID matches

### Example for Delete Operation

For:

`DELETE /journal/id/{id}`

the flow is:

1. Controller receives the ID
2. Service calls `deleteById(id)`
3. Repository deletes the matching document from MongoDB

## Case 3: Making Use of `ResponseEntity`

`ResponseEntity` is used when we want more control over the HTTP response.

Instead of returning only data, we can return:

- response body
- HTTP status code
- headers if needed

### Why `ResponseEntity` Is Useful

It helps us send proper HTTP responses like:

- `200 OK` -> request successful
- `201 CREATED` -> resource created
- `204 NO CONTENT` -> successful but no body
- `404 NOT FOUND` -> resource does not exist
- `500 INTERNAL SERVER ERROR` -> server-side issue
- all commands are given in notes 

### In This Project

The method:

- `GET /journal/id/{id}`

returns `ResponseEntity<JournalEntry>`

This means:

- if the journal entry exists, the API returns `200 OK` with the object
- if the journal entry does not exist, the API returns `404 NOT FOUND`

This is better than returning `null` because the client gets a proper HTTP message.

### Important Idea

Case 3 is not a separate project structure. It is an improvement in the controller layer.

The flow remains the same:

`Client -> Controller -> Service -> Repository -> MongoDB`

The only difference is that the controller sends richer and more meaningful HTTP responses.

## How All Three Cases Are Linked

These three cases can be understood as learning stages.

### Stage 1: In-memory Storage

- used for learning basics
- simple REST API
- no database dependency
- data is temporary

### Stage 2: MongoDB Integration

- adds permanent storage
- introduces repository layer
- uses configuration in `application.properties`
- better project structure

### Stage 3: `ResponseEntity`

- improves API response handling
- helps return proper status codes
- makes the API more professional and clear

So the progression is:

`In-memory API -> Database-backed API -> Better HTTP response handling`

## Final Summary

This project is built using standard Spring Boot layering.

- `JournalApp` starts the application
- `HealthCheck` checks whether the application is running
- `JournalEntry` is the model/entity
- `JournalEntryController2` handles HTTP requests
- `JournalEntryService` contains business logic
- `Repo` communicates with MongoDB
- `application.properties` stores MongoDB configuration

The three cases explained in this project are:

1. Using inbuilt storage without any external database
2. Integrating MongoDB for permanent storage
3. Using `ResponseEntity` for proper API responses

Together, these cases show how a Spring Boot project grows from a basic REST API into a cleaner and more professional backend application.
