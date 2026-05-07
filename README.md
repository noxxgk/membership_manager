# Gym Membership Manager API

A RESTful backend application for managing gym memberships, including gyms, membership plans, and members.
Created using Java 21 and Spring Boot 3.

## Technologies Used
* Java 21
* Spring Boot 3.x (Web, Data JPA, Validation)
* Maven
* H2 Database (In-memory database for local run)
* Lombok
* MapStruct
* Liquibase
* Springdoc OpenAPI (Swagger)
* Docker
* JUnit 5, Mockito & Testcontainers with PostgreSQL (Testing)

## How to Build and Run

### Option 1: Running locally using Maven
1. Clone the repository:
```
git clone <your-github-repo-url>
cd memberships_manager
```

2. Build the project using Maven:
```
mvn clean install
```

3. Run the application:
```
mvn spring-boot:run
```
The application will start on http://localhost:8080.

### Option 2: Running using Docker
1. Build the Docker image:
```
docker build -t memberships_manager .
```

2. Run the Docker container:
```
docker run -p 8080:8080 memberships_manager
```

## API Documentation (Swagger)
Once the application is running, you can access the interactive API documentation and test endpoints directly from the browser:
* Swagger UI: http://localhost:8080/swagger-ui.html
* OpenAPI JSON: http://localhost:8080/v3/api-docs

## Database Console (H2)
The application uses an in-memory H2 database managed via Liquibase migrations. You can inspect the tables and data via the browser:
* URL: http://localhost:8080/h2-console
* JDBC URL: `jdbc:h2:mem:gymdb`
* Username: `sa`
* Password: `password`

---

## REST API Endpoints & Sample Queries

### 1. Create a new gym
```
curl -X POST http://localhost:8080/api/gyms \
-H "Content-Type: application/json" \
-d "{\"name\":\"FitLife Center\", \"address\":\"Main St 123\", \"phoneNumber\":\"555-1234\"}"
```

### 2. List all gyms
```
curl -X GET http://localhost:8080/api/gyms
```

### 3. Create a new membership plan for a given gym (e.g., gymId = 1)
```
curl -X POST http://localhost:8080/api/gyms/1/plans \
-H "Content-Type: application/json" \
-d "{\"name\":\"BASIC PRO\", \"type\":\"BASIC\", \"monthlyPriceAmount\":99.99, \"currency\":\"PLN\", \"durationMonths\":12, \"maxMembers\":50}"
```

### 4. List all membership plans for a given gym (e.g., gymId = 1)
```
curl -X GET http://localhost:8080/api/gyms/1/plans
```

### 5. Register a new member to a given membership plan (e.g., planId = 1)
Capacity validation and uniqueness validation (fullName + email) are enforced via pessimistic locking and database constraints. The member is automatically set to ACTIVE.

```
curl -X POST http://localhost:8080/api/members \
-H "Content-Type: application/json" \
-d "{\"fullName\":\"John Doe\", \"email\":\"john.doe@example.com\", \"membershipPlanId\":1}"
```

### 6. List all members
Includes plan name, gym name, and status. Optimized with JOIN FETCH to prevent N+1 queries.

```
curl -X GET http://localhost:8080/api/members
```

### 7. Cancel a membership (e.g., memberId = 1)
Changes status to CANCELLED and frees up capacity in the plan.

```
curl -X PATCH http://localhost:8080/api/members/1/cancel
```

### 8. [Optional] Revenue Report
Displays total monthly revenue per gym, grouped by currency (only for ACTIVE members).

```
curl -X GET http://localhost:8080/api/reports/revenue
```
