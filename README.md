# FMZ CQRS Core

**FMZ CQRS Core** is a lightweight Java framework implementing the Command Query Responsibility Segregation (CQRS) pattern. It provides abstractions for commands, queries, handlers, dispatchers, and various result strategies. The framework allows you to separate the responsibility of modifying state (commands) from reading state (queries), enabling clean architecture, testability, and scalability.

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
   <version>2.0.3</version>
</dependency>
```

**Gradle:**
```groovy
implementation 'com.filipmikolajzeglen.cqrs:fmz-cqrs-core:1.0.0'
```

## Important for GitHub Packages

To fetch dependencies from GitHub Packages, you **must** add the following to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/filipmikolajzeglen</url>
  </repository>
</repositories>
```

And for **Gradle** (in your `build.gradle`):

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/filipmikolajzeglen")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME_GITHUB")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN_GITHUB")
        }
    }
}
```

Without this, Maven or Gradle will not be able to download dependencies from the GitHub repository.

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
   public <RESULT> RESULT handle(UserQuery query, ResultStrategy<User, RESULT> resultStrategy) {
      User user = findUserByName(query.name);
      return resultStrategy.expandSingle(user);
   }
}
```

**Register handlers and use the dispatcher:**
```java
List<CommandHandler<?, ?>> commandHandlers = List.of(new UserCreateCommandHandler());
List<QueryHandler<?, ?>> queryHandlers = List.of(new UserQueryHandler());
Dispatcher dispatcher = new DispatcherRegistry(commandHandlers, queryHandlers);

User created = dispatcher.execute(new UserCreateCommand("Alice"));
User found = dispatcher.perform(new UserQuery("Alice"), ResultStrategy.single());
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
        String subQueryResult = context.perform(new SubAutonomousQuery(), ResultStrategy.single());
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
    protected <RESULT> RESULT perform(AutonomousQueryContext context, ResultStrategy<String, RESULT> resultStrategy) {
        return resultStrategy.expandSingle("SubQuery");
    }
}
```

**Autonomous Query Example:**
```java
public class MainAutonomousQuery extends AutonomousQuery<String> {
   @Override
   protected <RESULT> RESULT perform(AutonomousQueryContext context, ResultStrategy<String, RESULT> resultStrategy) {
      // Perform a sub-query and get its result
      String subResult = context.perform(new SubAutonomousQuery(), ResultStrategy.single());
      // Compose the final result using the resultStrategy
      return resultStrategy.expandSingle("MainQuery -> " + subResult);
   }
}

public class SubAutonomousQuery extends AutonomousQuery<String> {
   @Override
   protected <RESULT> RESULT perform(AutonomousQueryContext context, ResultStrategy<String, RESULT> resultStrategy) {
      return resultStrategy.expandSingle("SubQuery");
   }
}
```

Autonomous commands and queries are useful for orchestration, chaining, or implementing logic that does not require a dedicated handler.

## Result Strategies

This library provides several built-in result strategies, each implementing the `ResultStrategy<DATA, RESULT>` interface. You can use them to control how query results are returned and shaped. Below is an overview of each strategy:

### 1. `SingleResultStrategy`
- **Type:** Returns a single element.
- **Behavior:** Throws `NoSuchElementException` if no element is found, or `IllegalStateException` if more than one element is found.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.single());
  ```

### 2. `OptionalResultStrategy`
- **Type:** Returns an `Optional<TYPE>`.
- **Behavior:** Returns `Optional.empty()` if no element is found, `Optional.of(element)` if one element is found, and throws `IllegalStateException` if more than one element is found.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.optional());
  ```

### 3. `ListResultStrategy`
- **Type:** Returns a `List<TYPE>`.
- **Behavior:** Returns all elements as a list (may be empty).
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.all());
  ```

### 4. `ExistResultStrategy`
- **Type:** Returns a `Boolean`.
- **Behavior:** Returns `true` if any element exists, otherwise `false`.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.exist());
  ```

### 5. `CountResultStrategy`
- **Type:** Returns a `Long`.
- **Behavior:** Returns the number of elements.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.count());
  ```

### 6. `FirstResultStrategy`
- **Type:** Returns an `Optional<TYPE>`.
- **Behavior:** Returns the first element as `Optional`, or `Optional.empty()` if the list is empty.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.first());
  ```

### 7. `PagedResultStrategy`
- **Type:** Returns a `PagedResult<TYPE>`.
- **Behavior:** Returns a page of elements, including metadata such as page number, size, total elements, and total pages.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.paged(0, 10, totalCount));
  ```

### 8. `SliceResultStrategy`
- **Type:** Returns a `SliceResult<TYPE>`.
- **Behavior:** Returns a slice (window) of elements with offset and limit, and a flag indicating if more elements are available.
- **Usage:**
  ```java
  dispatcher.perform(query, ResultStrategy.sliced(offset, limit));
  ```

### Example

```java
// Get a single result (throws if not exactly one)
MyEntity entity = dispatcher.perform(myQuery, ResultStrategy.single());

// Get all results as a list
List<MyEntity> entities = dispatcher.perform(myQuery, ResultStrategy.all());

// Get a paged result
PagedResult<MyEntity> page = dispatcher.perform(myQuery, ResultStrategy.paged(0, 10, totalCount));
```

Choose the result strategy that best fits your use case and pass it to the `perform` method of the dispatcher.

---

## Sorting in Result Strategies

Some result strategies support sorting of results. These implement the `OrderedResultStrategy` interface, which allows you to specify the order of returned elements by one or more properties.

### Supported ResultStrategy Types

Sorting is available for the following result strategy types:
- `ResultStrategy.all()` (`ListResultStrategy`)
- `ResultStrategy.first()` (`FirstResultStrategy`)
- `ResultStrategy.paged(...)` (`PagedResultStrategy`)
- `ResultStrategy.sliced(...)` (`SliceResultStrategy`)

### Defining Sorting

To specify sorting, use the `orderedByAsc(property)` or `orderedByDesc(property)` methods on the result strategy instance. You can chain multiple calls to set sorting by several fields (in priority order).

#### Example usage:

```java
// Sort ascending by the "name" field
List<MyEntity> entities = dispatcher.perform(
    myQuery,
    ResultStrategy.all().orderedByAsc("name")
);

// Sort descending by "createdAt", then ascending by "name"
PagedResult<MyEntity> page = dispatcher.perform(
    myQuery,
    ResultStrategy.paged(0, 10, totalCount)
        .orderedByDesc("createdAt")
        .orderedByAsc("name")
);
```

### Retrieving Sorting Information

You can retrieve the list of declared sort orders using the `getOrders()` method:

```java
OrderedResultStrategy<MyEntity, ?> resultStrategy = ResultStrategy.all()
    .orderedByAsc("name")
    .orderedByDesc("createdAt");

List<Order> orders = resultStrategy.getOrders();
for (Order order : orders) {
    System.out.println(order.getProperty() + " " + order.getDirection());
}
```

### Notes

- Sorting is declarative — it is up to the handler implementation or persistence layer to interpret and apply the sorting according to the specified fields.
- If you do not declare any sorting, results may be returned in any order.

---

## Advanced Result Strategies and Persistence

To support advanced result strategies and database operations, the [fmz-cqrs-persistence](https://github.com/filipmikolajzeglen/fmz-cqrs-persistence) module was created. It introduces dedicated abstractions:

- `DatabaseQuery`
- `DatabaseCommand`
- `DatabaseSuperCommand`

These types are designed for integration with persistence layers and provide additional features for advanced queries and transactional commands.

## Mentorship

- Paweł 'nivertius' Płazieński — [https://source.perfectable.org/](https://source.perfectable.org/)