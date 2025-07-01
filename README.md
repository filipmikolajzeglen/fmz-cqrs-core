Oto propozycja rozbudowanego README (po angielsku), zgodnie z Twoimi wytycznymi:

---

# FMZ CQRS Core

**FMZ CQRS Core** is a lightweight Java framework implementing the Command Query Responsibility Segregation (CQRS) pattern. It provides abstractions for commands, queries, handlers, dispatchers, and various pagination strategies. The framework allows you to separate the responsibility of modifying state (commands) from reading state (queries), enabling clean architecture, testability, and scalability.

## Overview

The CQRS pattern divides the system into two parts: **commands** (write operations) and **queries** (read operations). Each command or query is represented as a class. Handlers are responsible for processing these objects. The `Dispatcher` is the central interface for executing commands and performing queries. The framework supports interceptors for cross-cutting concerns such as logging or transactions.

In this CQRS implementation, commands and queries are further divided into two types: **business** and **resource-interacting**. Business commands and queries are always autonomous—they encapsulate business logic and orchestrate other commands or queries, but do not interact directly with external resources. Resource-interacting commands and queries are designed to do the minimal work necessary: they simply accept arguments in their constructor and execute their method, typically to interact with a database or external service. Autonomous commands can execute other commands and queries, while autonomous queries are restricted to invoking only other queries.

Historically, the CQRS pattern emerged from the work of Greg Young, who formalized the separation of command and query responsibilities to improve scalability, maintainability, and clarity in complex systems.
## Installation

Add the following dependency to your Maven or Gradle project.

**Maven:**
```xml
<dependency>
   <groupId>com.filipmikolajzeglen.cqrs</groupId>
   <artifactId>fmz-cqrs-core</artifactId>
   <version>1.0.0</version>
</dependency>
```

**Gradle:**
```groovy
implementation 'com.filipmikolajzeglen.cqrs:fmz-cqrs-core:1.0.0'
```

## Handler Registration in Spring and Micronaut

Handler registration is automatic when using the following integration modules:

- [fmz-cqrs-spring](https://github.com/filipmikolajzeglen/fmz-cqrs-spring) for Spring Boot applications
- [fmz-cqrs-micronaut](https://github.com/filipmikolajzeglen/fmz-cqrs-micronaut) for Micronaut applications

With these modules, all command and query handlers are discovered and registered automatically via dependency injection. You do not need to manually provide handler lists to the dispatcher.

## Example Usage

**Define a command:**
```java
public class UserCreateCommand extends Command<User> {
   public final String name;
   public UserCreateCommand(String name) {
      this.name = name;
   }
}
```

**Define a command handler:**
```java
public class UserCreateCommandHandler implements CommandHandler<UserCreateCommand, User> {
   @Override
   public User handle(UserCreateCommand command) {
      return new User(command.name);
   }
}
```

**Define a query:**
```java
public class UserQuery extends Query<User> {
   public final String name;
   public UserQuery(String name) {
      this.name = name;
   }
}
```

**Define a query handler:**
```java
public class UserQueryHandler implements QueryHandler<UserQuery, User> {
   @Override
   public <PAGE> PAGE handle(UserQuery query, Pagination<User, PAGE> pagination) {
      User user = findUserByName(query.name);
      return pagination.expandSingle(user);
   }
}
```

**Register handlers and use the dispatcher:**
```java
List<CommandHandler<?, ?>> commandHandlers = List.of(new UserCreateCommandHandler());
List<QueryHandler<?, ?>> queryHandlers = List.of(new UserQueryHandler());
Dispatcher dispatcher = new DispatcherRegistry(commandHandlers, queryHandlers);

User created = dispatcher.execute(new UserCreateCommand("Alice"));
User found = dispatcher.perform(new UserQuery("Alice"), Pagination.single());
```

You can also use interceptors for logging or transactions by wrapping the dispatcher with `DispatcherDecorator`.

## Autonomous Commands and Queries

FMZ CQRS Core supports **Autonomous Commands** and **Autonomous Queries**. These are special types of commands and queries that can execute logic independently, without requiring a handler. They can also chain other commands and queries internally.

**Autonomous Command Example:**
```java
public class MainAutonomousCommand extends AutonomousCommand<String> {
    @Override
    protected String execute(AutonomousCommandContext context) {
        // Execute a sub-command
        String subCommandResult = context.execute(new SubAutonomousCommand());
        // Perform a query and get a single result
        String subQueryResult = context.perform(new SubAutonomousQuery(), Pagination.single());
        return "MainCommand -> " + subCommandResult + " -> " + subQueryResult;
    }
}

public class SubAutonomousCommand extends AutonomousCommand<String> {
    @Override
    protected String execute(AutonomousCommandContext context) {
        return "SubCommand";
    }
}

public class SubAutonomousQuery extends AutonomousQuery<String> {
    @Override
    protected <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<String, PAGE> pagination) {
        return pagination.expandSingle("SubQuery");
    }
}
```

**Autonomous Query Example:**
```java
public class MainAutonomousQuery extends AutonomousQuery<String> {
   @Override
   protected <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<String, PAGE> pagination) {
      // Perform a sub-query and get its result
      String subResult = context.perform(new SubAutonomousQuery(), Pagination.single());
      // Compose the final result using the pagination strategy
      return pagination.expandSingle("MainQuery -> " + subResult);
   }
}

public class SubAutonomousQuery extends AutonomousQuery<String> {
   @Override
   protected <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<String, PAGE> pagination) {
      return pagination.expandSingle("SubQuery");
   }
}
```

Autonomous commands and queries are useful for orchestration, chaining, or implementing logic that does not require a dedicated handler.

## Pagination Strategies

This library provides several built-in pagination strategies, each implementing the `Pagination<DATA, PAGE>` interface. You can use them to control how query results are returned and shaped. Below is an overview of each strategy:

### 1. `SinglePagination`
- **Type:** Returns a single element.
- **Behavior:** Throws `NoSuchElementException` if no element is found, or `IllegalStateException` if more than one element is found.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.single());
  ```

### 2. `OptionalPagination`
- **Type:** Returns an `Optional<TYPE>`.
- **Behavior:** Returns `Optional.empty()` if no element is found, `Optional.of(element)` if one element is found, and throws `IllegalStateException` if more than one element is found.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.optional());
  ```

### 3. `ListPagination`
- **Type:** Returns a `List<TYPE>`.
- **Behavior:** Returns all elements as a list (may be empty).
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.all());
  ```

### 4. `ExistPagination`
- **Type:** Returns a `Boolean`.
- **Behavior:** Returns `true` if any element exists, otherwise `false`.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.exist());
  ```

### 5. `CountPagination`
- **Type:** Returns a `Long`.
- **Behavior:** Returns the number of elements.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.count());
  ```

### 6. `FirstPagination`
- **Type:** Returns an `Optional<TYPE>`.
- **Behavior:** Returns the first element as `Optional`, or `Optional.empty()` if the list is empty.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.first());
  ```

### 7. `PagedPagination`
- **Type:** Returns a `PagedResult<TYPE>`.
- **Behavior:** Returns a page of elements, including metadata such as page number, size, total elements, and total pages.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.paged(page, size, totalCount));
  ```

### 8. `SlicePagination`
- **Type:** Returns a `SliceResult<TYPE>`.
- **Behavior:** Returns a slice (window) of elements with offset and limit, and a flag indicating if more elements are available.
- **Usage:**
  ```java
  dispatcher.perform(query, Pagination.sliced(offset, limit));
  ```

### Example

```java
// Get a single result (throws if not exactly one)
MyEntity entity = dispatcher.perform(myQuery, Pagination.single());

// Get all results as a list
List<MyEntity> entities = dispatcher.perform(myQuery, Pagination.all());

// Get a paged result
PagedResult<MyEntity> page = dispatcher.perform(myQuery, Pagination.paged(0, 10, totalCount));
```

Choose the pagination strategy that best fits your use case and pass it to the `perform` method of the dispatcher.

## Advanced Pagination and Persistence

To support advanced pagination and database operations, the [fmz-cqrs-persistence](https://github.com/filipmikolajzeglen/fmz-cqrs-persistence) module was created. It introduces dedicated abstractions:

- `DatabaseQuery`
- `DatabaseCommand`
- `DatabaseSuperCommand`

These types are designed for integration with persistence layers and provide additional features for paginated queries and transactional commands.

## Mentorship

- Paweł 'nivertius' Płazieński — [https://source.perfectable.org/](https://source.perfectable.org/)