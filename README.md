# Spring Modulith CQRS Example

This project demonstrates how to implement the Command Query Responsibility Segregation (CQRS) pattern using Spring Modulith to create clear boundaries between application modules.

## Project Structure

The application is organized into distinct modules:

- **Catalog Command Module**: Handles write operations and business logic
- **Catalog Query Module**: Optimized for read operations
- **Shared Events**: Cross-cutting event definitions for module communication

## Key Features

- **Clear Module Boundaries**: Using Spring Modulith's `@ApplicationModule` annotations
- **Event-Based Communication**: Modules communicate through events, not direct dependencies
- **Optimized Query Model**: Separate repository for read operations with caching hints
- **Business Logic Isolation**: All business rules reside in the command side
- **Clean API Separation**: Distinct endpoints for commands and queries

## Technology Stack

- Java 23
- Spring Boot 3.4.3
- Spring Modulith 1.3.3
- Spring Data JPA
- H2 Database (for development)

## Running the Application

To run the application:

```bash
./gradlew bootRun
```

## Testing

The project includes integration tests that demonstrate the CQRS flow:

```bash
./gradlew test
```

The tests show:
1. How commands modify the write model
2. How events propagate changes to the read model
3. How to query the optimized read model

## API Endpoints

### Command API

- `POST /api/products/commands` - Create a new product
- `PUT /api/products/commands/{id}` - Update a product
- `PATCH /api/products/commands/{id}/stock` - Update product stock

### Query API

- `GET /api/products/queries` - Get all products
- `GET /api/products/queries/summaries` - Get product summaries 
- `GET /api/products/queries/{id}` - Get product by ID
- `GET /api/products/queries/by-category?category=Electronics` - Get products by category
- `GET /api/products/queries/by-price?min=10&max=50` - Get products by price range

## Module Documentation

Spring Modulith automatically generates documentation for the application modules. Run the `ModulithStructureTest` tests to generate these diagrams. 