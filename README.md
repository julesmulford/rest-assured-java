# REST Assured Java API Test Framework

REST API test automation framework built with **REST Assured 5** + **Java 21** + **JUnit 5** + **Allure** targeting the [reqres.in](https://reqres.in) public API.

Suggested GitHub repository name: `rest-assured-java`

---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 21 |
| Maven | 3.9+ |
| REST Assured | 5.4.0 |
| JUnit Jupiter | 5.10.2 |
| Allure JUnit5 | 2.27.0 |
| AssertJ | 3.25.3 |
| Jackson Databind | 2.17.1 |
| Log4j2 | 2.23.1 |

---

## Prerequisites

- **Java 21** — [Download Temurin](https://adoptium.net/)
- **Maven 3.9+** — [Download Maven](https://maven.apache.org/download.cgi)
- Internet access to reach `https://reqres.in`

Verify your environment:

```bash
java -version
mvn -version
```

---

## Running Tests

### All tests

```bash
mvn test
```

### Smoke tests only

```bash
mvn test -Dgroups=smoke
```

### Regression tests only

```bash
mvn test -Dgroups=regression
```

### Override the base URL

```bash
API_BASE_URL=https://reqres.in mvn test
```

---

## Allure Reporting

### Generate and open the report in a local browser

```bash
mvn allure:serve
```

### Generate static HTML report

```bash
mvn allure:report
# Report written to: target/site/allure-maven-plugin/index.html
```

---

## Project Architecture

```
rest-assured-java/
├── .github/workflows/ci.yml         # GitHub Actions CI pipeline
├── pom.xml                          # Maven build descriptor
└── src/
    ├── main/
    │   ├── resources/log4j2.xml     # Log4j2 console logging config
    │   └── java/com/reqres/
    │       ├── config/
    │       │   └── ApiConfig.java   # Base URL + timeout from env vars
    │       ├── models/              # Jackson-annotated POJO request/response models
    │       │   ├── User.java
    │       │   ├── UserData.java
    │       │   ├── UserListResponse.java
    │       │   ├── CreateUserRequest.java
    │       │   ├── CreateUserResponse.java
    │       │   ├── LoginRequest.java
    │       │   └── LoginResponse.java
    │       └── specs/
    │           ├── RequestSpecs.java   # Reusable REST Assured request spec
    │           └── ResponseSpecs.java  # Reusable REST Assured response specs
    └── test/
        ├── resources/allure.properties
        └── java/com/reqres/
            ├── base/
            │   └── BaseApiTest.java    # JUnit 5 base class with spec setup
            └── tests/
                ├── UsersGetTest.java          # GET /api/users, GET /api/users/{id}
                ├── UsersCreateUpdateTest.java # POST, PUT, PATCH /api/users
                ├── UsersDeleteTest.java       # DELETE /api/users/{id}
                └── AuthTest.java              # POST /api/login, POST /api/register
```

### Key Design Decisions

**RequestSpecs / ResponseSpecs** — Centralised REST Assured builder configuration prevents duplication. Every test receives an identical base specification including content type, base URI, Allure filter and logging filters.

**AllureRestAssured filter** — Registered in `RequestSpecs.defaultSpec()`, it automatically captures full request and response details as Allure attachments for every test, with no per-test boilerplate.

**POJO models with Jackson** — `@JsonIgnoreProperties(ignoreUnknown = true)` and `@JsonProperty` on snake_case fields provide clean, type-safe deserialization via `.extract().as(...)`.

**BaseApiTest** — Abstract JUnit 5 base class with `@BeforeAll` for global RestAssured configuration and `@BeforeEach` for per-test spec initialization and Allure parameter logging.

**Tag-based execution** — Tests are tagged `smoke` (happy path) or `regression` (edge cases), enabling targeted CI pipeline stages.

---

## API Coverage

| Method | Endpoint | Test class | Tag |
|---|---|---|---|
| GET | `/api/users?page=2` | `UsersGetTest` | smoke |
| GET | `/api/users/{id}` | `UsersGetTest` | smoke |
| GET | `/api/users/23` (404) | `UsersGetTest` | regression |
| POST | `/api/users` | `UsersCreateUpdateTest` | smoke |
| PUT | `/api/users/{id}` | `UsersCreateUpdateTest` | regression |
| PATCH | `/api/users/{id}` | `UsersCreateUpdateTest` | regression |
| DELETE | `/api/users/{id}` | `UsersDeleteTest` | smoke |
| POST | `/api/login` (success) | `AuthTest` | smoke |
| POST | `/api/register` (success) | `AuthTest` | smoke |
| POST | `/api/login` (bad creds) | `AuthTest` | regression |

---

## CI/CD

The GitHub Actions workflow at `.github/workflows/ci.yml` defines a sequential four-stage pipeline:

1. **Build & Compile** — Compiles main and test sources; fails fast on syntax errors.
2. **Smoke Tests** — Runs `@Tag("smoke")` tests; uploads Allure results as an artifact.
3. **Regression Tests** — Runs `@Tag("regression")` tests after smoke passes; uploads Allure results.
4. **Allure Report** — Downloads regression results, generates the HTML report, and uploads as an artifact.

All four stages use Java 21 Temurin with Maven dependency caching for fast execution.

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `API_BASE_URL` | `https://reqres.in` | Base URL for the API under test |
| `CONNECT_TIMEOUT_MS` | `10000` | HTTP connection timeout in milliseconds |
