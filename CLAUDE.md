# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Build
```bash
mvn clean install
```

### Run the application
```bash
mvn spring-boot:run
```

### Run all tests
```bash
mvn test
```

### Run a single test class
```bash
mvn test -Dtest=CustomerServiceTest
```

### Run a single test method
```bash
mvn test -Dtest=CustomerServiceTest#createShouldReturnACustomerResponseDto
```

## Architecture

Spring Boot 3.5.5 REST API (Java 21) with a standard layered architecture:

```
Controller → Service → Repository (Spring Data JPA) → PostgreSQL
```

### Layers

- **`controller/`** — `@RestController` classes. Each method delegates directly to a service; no business logic here. Annotated with SpringDoc `@Operation`/`@ApiResponse` for Swagger documentation.
- **`service/`** — Business logic. Uniqueness checks (CPF, phone number) happen here before reaching the DB. Delete operations also check for FK dependencies via other repositories before deleting.
- **`repository/`** — Spring Data JPA `JpaRepository` interfaces. Custom `existsBy*` queries are derived from method names.
- **`entity/`** — JPA entities (`Customer`, `Sale`, `SaleProduct`, `Product`). No Lombok — plain getters/setters.
- **`dto/`** — Separate `*RequestDto` (input) and `*ResponseDto` (output) per entity.
- **`mapper/`** — MapStruct interfaces (`@Mapper(componentModel = "spring")`). Implementations are generated at compile time.
- **`exception/`** — Custom exceptions per entity (e.g., `CustomerNotFoundException`, `CpfAlreadyExistsException`) plus a shared `EntityInUseException` for FK-guard failures.
- **`exceptionhandler/`** — Global `@ControllerAdvice` exception handler that maps the custom exceptions to HTTP responses.

### Database

- **Production**: PostgreSQL on `localhost:5432`, database `pm_database`, user `postgres`, password `123456` (configured in `application.properties`).
- **Tests**: H2 in-memory, configured in `src/test/resources/application.properties` (`jdbc:h2:mem:teste_int_db`, DDL strategy `create-drop`).

### Domain model

`Customer` ←→ `Sale` ←→ `SaleProduct` ←→ `Product`

A `Sale` tracks status (`SaleStatus` enum), total amount, installment count/amount, and date. `SaleProduct` is the join table between `Sale` and `Product`.

## Testing conventions

Three test layers, each with a distinct Spring annotation:

| Layer | Annotation | What runs |
|---|---|---|
| Service (unit) | `@ExtendWith(MockitoExtension.class)` | Mockito mocks — no Spring context |
| Repository (integration) | `@DataJpaTest` | H2 + `TestEntityManager` |
| Controller (full integration) | `@SpringBootTest` + `@AutoConfigureMockMvc` + `@Transactional` | Full context, MockMvc, H2 |

**Builder pattern** for test fixtures: `src/test/java/com/alessandromelo/builders/` contains one Builder class per entity/DTO (e.g., `CustomerBuilder`, `CustomerRequestDtoBuilder`, `CustomerResponseDtoBuilder`). Each builder has sensible defaults and fluent `with*` methods — use these instead of constructing objects inline in tests.

Test method names follow the pattern: `methodName_ShouldDoSomething` (e.g., `createShouldThrowCpfAlreadyExistsException`). Use `@DisplayName` for human-readable descriptions.

## API documentation

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when the app is running.
