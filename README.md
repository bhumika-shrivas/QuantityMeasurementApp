# ✅ UC15: N-Tier Architecture Refactoring for Quantity Measurement Application

## 📖 Description

UC15 refactors the Quantity Measurement Application into a **clean N-Tier architecture** to improve maintainability, scalability, and separation of concerns.

Previous use cases (UC1–UC14) implemented measurement logic inside a single application layer. While functional, this structure mixed responsibilities such as input handling, business logic, and data representation.

UC15 restructures the system into layered architecture consisting of:

- Controller Layer
- Service Layer
- Repository Layer
- Entity / Model Layer
- Data Transfer Objects (DTO)

This refactoring introduces **professional application architecture patterns** while preserving all existing measurement functionality.

---

## 🎯 Objective

- Refactor the application into **N-Tier architecture**
- Introduce **DTO objects** for data transfer
- Introduce **Service layer** for business logic
- Introduce **Repository layer** for data storage
- Introduce **Entity layer** for operation records
- Improve **testability and scalability**
- Preserve all functionality from **UC1–UC14**
- Ensure clear separation of responsibilities

---

## 🏗 N-Tier Architecture

The application now follows the layered architecture below:


Application Layer
↓
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Entity / Model Layer


Each layer performs a specific role and communicates through clearly defined interfaces.

---

## 🔹 Controller Layer


QuantityMeasurementController


Responsibilities:

- Accept `QuantityDTO` input objects
- Validate input parameters
- Delegate operations to service layer
- Display results or error messages

Supported operations:

- Comparison
- Conversion
- Addition
- Subtraction
- Division

The controller contains **no business logic**.

---

## 🔹 Service Layer


IQuantityMeasurementService
QuantityMeasurementServiceImpl


Responsibilities:

- Perform all measurement operations
- Convert DTO objects to internal models
- Validate category compatibility
- Execute arithmetic operations
- Handle exceptions
- Create entity records for repository

The service layer represents the **core business logic** of the application.

---

## 🔹 Repository Layer


IQuantityMeasurementRepository
QuantityMeasurementCacheRepository


Responsibilities:

- Store operation records
- Maintain in-memory cache
- Persist history using serialization

Design Pattern used:


Singleton Pattern


Only one repository instance exists to manage stored measurement operations.

---

## 🔹 Data Transfer Objects (DTO)


QuantityDTO


Purpose:

- Transfer data between controller and service layers

Fields typically include:

- value
- unit
- measurement type

DTO objects contain **no business logic**.

---

## 🔹 Entity Layer


QuantityMeasurementEntity


Represents a stored record of a measurement operation.

Contains information such as:

- operand values
- operation type
- result
- error message

Implements:


Serializable


so operation history can be persisted.

---

## 🔄 Example Application Flow

Example: **Length Equality Comparison**


Controller receives QuantityDTO objects

Controller calls Service.compare()

Service converts DTO → QuantityModel

Service performs equality check

Service creates QuantityMeasurementEntity

Repository stores operation

Controller prints result


---

## 🧪 Example Demonstrations

### 🔹 Example 1 — Length Equality


2 ft == 24 in


Output:


--- Equality Demonstration ---
Operation: COMPARISON
This Quantity: 2.0 FEET
That Quantity: 24.0 INCHES
Comparison Result: true


---

### 🔹 Example 2 — Temperature Conversion


0°C → Fahrenheit


Output:


Temperature conversion result: 32°F


---

### 🔹 Example 3 — Cross-Category Operation Prevention

Attempt:


2 ft + 10 kg


Output:


❌ Error: Cannot perform arithmetic between different measurement categories:
LengthUnit and WeightUnit


The service layer validates category compatibility before performing arithmetic.

---

## 🔒 Cross-Category Safety

The system prevents arithmetic operations across incompatible measurement domains.

Examples:


Length + Weight → Invalid
Length + Temperature → Invalid
Weight + Volume → Invalid


These operations throw `IllegalArgumentException`.

---

## 📤 Postconditions

- Application logic is separated into layers
- DTO objects standardize communication between layers
- Repository stores operation history
- Service layer centralizes business logic
- Existing measurement features remain unchanged
- System becomes scalable and testable

---

## 🧪 Key Concepts Tested

### 🏗 Architecture Concepts

- N-Tier Architecture
- Separation of Concerns
- Layered System Design

### 🔁 Design Patterns

- Singleton Pattern
- Dependency Injection
- DTO Pattern

### 📐 SOLID Principles

- Single Responsibility Principle (SRP)
- Open–Closed Principle (OCP)
- Liskov Substitution Principle (LSP)
- Interface Segregation Principle (ISP)
- Dependency Inversion Principle (DIP)

---

## 🧠 Concepts Learned

- N-Tier architecture design
- Service-oriented architecture
- Data Transfer Object pattern
- Repository abstraction
- Dependency injection
- Error handling as data
- Scalable application structure

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

---

## 🔥 Key Achievement

UC15 transforms the Quantity Measurement Application from a **single-layer demonstration program** into a **structured multi-layer architecture**.

This refactoring enables:

- better maintainability
- easier testing
- scalable system design
- readiness for enterprise applications

The system is now prepared for future extensions such as:

- REST APIs
- database persistence
- web interfaces
- microservices architecture.

---
