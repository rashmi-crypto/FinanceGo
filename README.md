# FinanceGo

> A privacy-first personal finance tracker. Your money data stays yours.

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Build](https://img.shields.io/badge/build-Maven-red)
![Status](https://img.shields.io/badge/status-in%20development-yellow)

FinanceGo is a RESTful backend for tracking **income, expenses, savings, and investments** in one place. It is built with a privacy-first mindset: minimal data collection, secure authentication, and no third-party data sharing.

This repository currently contains the **backend API**. A web frontend (React or Angular) and a possible Android client are planned for later phases.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Overview](#api-overview)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Project Structure](#project-structure)
- [What I'm Learning](#what-im-learning)
- [License](#license)

---

## Features

Status key: ✅ done · 🚧 in progress · 🗓️ planned

| Feature | Status |
|---|---|
| Project setup and MySQL connection | ✅ |
| User registration and login | ✅ |
| Secure authentication with Spring Security (JWT) | 🗓️ |
| Income and expense tracking (CRUD) | 🗓️ |
| Categories for transactions | 🗓️ |
| Savings goals | 🗓️ |
| Investment tracking | 🗓️ |
| Monthly summaries and reports | 🗓️ |
| Pagination, filtering and sorting | 🗓️ |
| Global exception handling and validation | 🗓️ |

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4 |
| Persistence | Spring Data JPA (Hibernate) |
| Security | Spring Security |
| Database | MySQL |
| Build tool | Maven |
| Testing | JUnit 5, Mockito |

## Architecture

FinanceGo follows a classic **layered architecture** so each part has one clear responsibility:

```
Client  →  Controller  →  Service  →  Repository  →  MySQL
              │              │            │
             DTOs      Business logic   JPA entities
```

- **Controller layer**: exposes REST endpoints and handles HTTP concerns only.
- **Service layer**: contains business rules and transactions.
- **Repository layer**: data access through Spring Data JPA.
- **DTOs**: separate API request/response shapes from database entities.
- **Dependency Injection**: constructor injection throughout, which keeps classes easy to test.

## Getting Started

### Prerequisites

- JDK 25
- Maven 3.9+
- MySQL 8+

### Setup

```bash
# 1. Clone the repository
git clone https://github.com/rashmi-crypto/FinanceGo.git
cd FinanceGo

# 2. Create the database
mysql -u root -p -e "CREATE DATABASE financego;"

# 3. Configure credentials (see Configuration below)

# 4. Run the application
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.

## Configuration

Database credentials are read from environment variables so secrets never get committed.

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | JDBC URL | `jdbc:mysql://localhost:3306/financego` |
| `DB_USERNAME` | Database user | `root` |
| `DB_PASSWORD` | Database password | `your_password` |
| `JWT_SECRET` | Signing key for tokens (once auth is added) | `a-long-random-string` |

> **Never commit real passwords or secrets.** Keep local overrides in an untracked file such as `application-local.properties`.

## API Overview

> Planned endpoints. This section will be updated as each one is built.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create an account |
| POST | `/api/auth/login` | Log in and receive a token |
| GET | `/api/transactions` | List transactions (paginated) |
| POST | `/api/transactions` | Add income or expense |
| PUT | `/api/transactions/{id}` | Update a transaction |
| DELETE | `/api/transactions/{id}` | Delete a transaction |
| GET | `/api/summary/monthly` | Monthly income vs. expense summary |

## Testing

```bash
./mvnw test
```

- **Unit tests** for the service layer using JUnit 5 and Mockito.
- **Repository tests** against a test database.
- **Controller tests** with MockMvc.

## Roadmap

- [x] **Phase 1**: Project setup, entities, repositories
- [ ] **Phase 2**: Transaction CRUD with DTOs and validation
- [ ] **Phase 3**: Authentication and authorization (Spring Security)
- [ ] **Phase 4**: Savings goals and investment tracking
- [ ] **Phase 5**: Reports, filtering, pagination
- [ ] **Phase 6**: API documentation (OpenAPI/Swagger) and Docker support
- [ ] **Phase 7**: Web frontend (React or Angular)
- [ ] **Phase 8**: Android client (Retrofit)

## Project Structure

> Will evolve as the project grows.

```
src/main/java/com/financego/
├── controller/
├── service/
├── repository/
├── model/        # JPA entities
├── dto/
├── config/       # Security and app configuration
└── exception/
```

## What I'm Learning

This is my first full-stack portfolio project, built to practice professional backend engineering: layered architecture, REST design, secure authentication, and test-driven habits with the Spring ecosystem.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

Built by **Rashmi** · [GitHub](https://github.com/rashmi-crypto) 
