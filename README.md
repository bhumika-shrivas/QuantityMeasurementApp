# ✅ UC16: Database Integration with JDBC for Quantity Measurement Persistence

## 📖 Description

UC16 extends the Quantity Measurement Application by introducing **persistent database storage using JDBC**.

In UC15, the application implemented a clean **N-Tier architecture**, but the repository layer stored measurement history only in **in-memory cache with optional serialization**. This approach had several limitations such as limited scalability, lack of concurrent access management, and difficulty in querying stored data.

UC16 enhances the repository layer by integrating a **relational database using JDBC** so that measurement operations are permanently stored.

The application now supports:

- JDBC database communication
- Connection pooling
- SQL-based data persistence
- Automatic schema creation
- Professional Maven project configuration
- Structured logging
- Database-backed repository implementation

This upgrade moves the application closer to **enterprise-grade architecture**.

---

## 🎯 Objective

- Integrate **database persistence using JDBC**
- Replace in-memory repository with **database repository implementation**
- Store measurement operations in a **relational database**
- Introduce **connection pooling**
- Implement **SQL-based storage**
- Configure project using **Maven dependencies**
- Maintain full compatibility with **UC1–UC15 functionality**
- Enable **operation history tracking**

---

## 🏗 Updated Architecture

The application now follows the architecture below:


Application Layer
↓
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database (H2)


The repository layer communicates with the database using **JDBC connections managed by a connection pool**.

---

## 🔹 Controller Layer


QuantityMeasurementController


Responsibilities:

- Accept `QuantityDTO` input objects
- Validate user inputs
- Call service layer operations
- Return formatted results to the application

Supported operations include:

- Comparison
- Conversion
- Addition
- Subtraction
- Division

The controller **does not interact with the database directly**.

---

## 🔹 Service Layer


IQuantityMeasurementService
QuantityMeasurementServiceImpl


Responsibilities:

- Execute all measurement operations
- Convert DTO objects to internal domain objects
- Validate measurement categories
- Perform arithmetic logic
- Handle exceptions
- Create operation entities for persistence

The service layer now **delegates storage responsibilities to the repository layer**, without knowing whether storage is in memory or database.

---

## 🔹 Repository Layer


IQuantityMeasurementRepository
QuantityMeasurementDatabaseRepository


UC16 introduces a new repository implementation:


QuantityMeasurementDatabaseRepository


Responsibilities:

- Persist measurement operations using JDBC
- Execute SQL queries
- Manage database connections through a connection pool
- Retrieve stored measurement records
- Handle database exceptions

SQL operations use **PreparedStatement** to prevent SQL injection.

---

## 🔹 Database Layer

UC16 introduces a relational database using:


H2 In-Memory Database


The database schema is automatically created using:


schema.sql


### Table Structure

| Column | Description |
|------|-------------|
| id | Unique record identifier |
| operand1 | First measurement operand |
| operand2 | Second measurement operand |
| operation_type | Operation performed |
| result | Operation result |
| error_message | Error details if operation fails |
| timestamp | Time of operation |

Each measurement operation is stored as a database record.

---

## 🔹 Connection Pool

Database connections are managed using:


HikariCP


Benefits:

- Efficient connection reuse
- Improved application performance
- Reduced connection overhead
- Production-grade connection management

The connection pool initializes when the application starts and shuts down when the application exits.

---

## 🔹 Logging Framework

UC16 introduces structured logging using:


SLF4J + Logback


Logging is used for:

- database initialization
- connection pool status
- system events
- debugging information

---

## 🔄 Example Application Flow

Example: **Length Equality Comparison**


Controller receives QuantityDTO objects

Controller calls Service.compare()

Service converts DTO → Quantity domain model

Service performs equality check

Service creates QuantityMeasurementEntity

Repository executes SQL INSERT

Database stores measurement record

Controller prints result


---

## 🧪 Example Demonstrations

### 🔹 Example 1 — Length Equality

Input:


2 ft == 24 in


Output:


Comparison result: Result: value=0.0, unit=FEET, type=LENGTH


Database Record:


operand1: 2 FEET
operand2: 24 INCHES
operation_type: COMPARE
result: Result: value=0.0, unit=FEET, type=LENGTH


---

### 🔹 Example 2 — Temperature Conversion

Input:


0°C → Fahrenheit


Output:


Temperature conversion result: Result: value=32.0, unit=FAHRENHEIT, type=TEMPERATURE


Database Record:


operation_type: CONVERT
result: 32°F


---

### 🔹 Example 3 — Cross Category Operation Prevention

Attempt:


2 ft + 10 kg


Output:


Cross-category addition not supported: Cross-category operation not allowed


The failed operation is also recorded with an **error message**.

---

## 🔒 Data Integrity

UC16 ensures secure and consistent database operations using:

- Prepared SQL statements
- Connection pooling
- Exception handling
- Repository abstraction

---

## 📤 Postconditions

- Database persistence is enabled
- Measurement operations are stored in relational tables
- Application architecture remains layered
- Repository implementation can switch between cache and database
- All UC1–UC15 functionality continues to work
- Application becomes ready for enterprise data storage

---

## 🧪 Key Concepts Tested

### 🏗 Architecture Concepts

- N-Tier Architecture
- Repository Pattern
- Layered Application Design

### 🗄 Database Concepts

- JDBC API
- SQL Persistence
- Connection Pooling
- Database Schema Management

### 🔁 Design Patterns

- Repository Pattern
- Dependency Injection
- DTO Pattern

---

## 🧠 Concepts Learned

- JDBC database integration
- Connection pool management
- SQL query execution
- Persistent data storage
- Database-backed repository design
- Logging configuration
- Maven dependency management

---

## 🚀 Architectural Evolution

| Use Case | Capability Added |
|----------|------------------|
| UC1 | Feet equality |
| UC2 | Inch equality |
| UC3 | Generic Length |
| UC4 | Yard support |
| UC5 | Unit conversion |
| UC6 | Unit addition |
| UC7 | Explicit target addition |
| UC8 | Standalone units |
| UC9 | Weight management |
| UC10 | Generic quantity architecture |
| UC11 | Volume measurement |
| UC12 | Subtraction & Division |
| UC13 | Centralized arithmetic logic |
| UC14 | Temperature measurement |
| UC15 | N-Tier architecture refactoring |
| UC16 | Database persistence using JDBC |

---

## 🔥 Key Achievement

UC16 upgrades the application from an **in-memory demonstration system** to a **database-backed enterprise-style architecture**.

The system now supports:

- persistent storage of operations
- scalable data management
- structured logging
- production-grade connection pooling

This prepares the application for future extensions such as:

- REST APIs
- Spring Boot integration
- Web interfaces
- distributed microservices
- advanced analytics on measurement history.

---
