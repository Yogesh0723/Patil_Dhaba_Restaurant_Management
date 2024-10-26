# Patil Dhaba Restaurant Management API

The Patil Dhaba Restaurant Management API is a RESTful service designed to manage tables, order items, and calculate profits for a restaurant. This API provides endpoints to create tables, add order items, apply discounts, clear tables, retrieve table details, and get today's profit.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Setup Instructions](#setup-instructions)
- [API Endpoints](#api-endpoints)
- [Running the Application](#running-the-application)
- [Contributing](#contributing)
- [License](#license)

## Features

- Create and manage tables
- Add order items to tables
- Apply discounts to tables
- Clear tables (remove all order items and reset totals)
- Retrieve a table's details
- Calculate today's profit

## Technologies

The backend is developed using:

- Spring Boot
- MongoDB (as the database)
- Lombok (for reducing boilerplate code)
- Spring Data MongoDB (for interacting with MongoDB)
- Spring MVC (for RESTful endpoints)

## Setup Instructions

### Prerequisites

Ensure you have the following installed:

- Java 20
- Maven
- MongoDB (running locally or connection URI available)

### Clone the Repository

```bash
git clone <repository-url>
cd PatilDhaba
```

### Install Dependencies

```bash
mvn clean install
```

### Application Configuration

Configure the application properties in `src/main/resources/application.properties`:

```properties
spring.application.name=PatilDhaba

# Mongo DB
spring.data.mongodb.uri=mongodb://localhost:27017/patilDB
spring.mongodb.embedded.version=3.0.0
server.port=8081

# Enable health and info endpoints
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

Make sure MongoDB is running and replace the `spring.data.mongodb.uri` with your MongoDB connection URI if needed.

## Running the Application

Start the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8081`.

## API Endpoints

### Table Management

- **Create Table**

  - **URL:** `/tables/create`
  - **Method:** POST
  - **Query Parameter:** `tableNumber`
  - **Description:** Creates a new table with the specified table number.

  ```bash
  curl -X POST "http://localhost:8081/tables/create?tableNumber=1"
  ```

- **Add Order Item to Table**

  - **URL:** `/tables/orderItem/add/{tableNumber}`
  - **Method:** POST
  - **Path Variable:** `tableNumber`
  - **Body:**
    ```json
    {
      "name": "Naan",
      "price": 20.0,
      "quantity": 5
    }
    ```
  - **Description:** Adds an order item to the specified table.

- **Apply Discount to Table**

  - **URL:** `/tables/discount/{tableNumber}`
  - **Method:** PUT
  - **Path Variable:** `tableNumber`
  - **Query Parameter:** `discount`
  - **Description:** Applies a discount to the specified table.

- **Clear Table**

  - **URL:** `/tables/clear/{tableNumber}`
  - **Method:** DELETE
  - **Path Variable:** `tableNumber`
  - **Description:** Clears the specified table, removing all order items and resetting totals.

- **Get Table Details**

  - **URL:** `/tables/{tableNumber}`
  - **Method:** GET
  - **Path Variable:** `tableNumber`
  - **Description:** Retrieves details of the specified table.

### Profit Calculation

- **Get Today's Profit**

  - **URL:** `/tables/profit/today`
  - **Method:** GET
  - **Description:** Calculates and retrieves today's profit.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any changes or enhancements.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
