
## Project Description

This is an enterprise-grade, lightweight Spring Boot web application for managing student academic marks and publishing semester transcripts. It serves as the primary artifact for demonstrating professional software testing, verification, and validation activities, spanning the entire course syllabus:
- Systematic black-box test design (Equivalence Partitioning, Boundary Value Analysis, Decision Table testing, State Transition testing).
- White-box branch and instruction coverage analysis (JaCoCo).
- Multi-tier automated testing following the Test Pyramid (JUnit 5, Mockito test doubles, Spring Data JPA integration, MockMvc, and Selenium WebDriver).
- Automated CI pipelines with GitHub Actions and Dockerized Jenkins.
- Defect tracking and quality metrics calculation (Defect Density, Defect Removal Efficiency).

---

## Technology Stack

- **Language & Framework:** Java 17, Spring Boot 3.3.5
- **Build & Dependency Management:** Maven 3
- **Presentation & Styling:** Thymeleaf, Minimalist Modern CSS Design System
- **Database & Persistence:** H2 In-Memory Database, Spring Data JPA, Hibernate
- **Security:** Spring Security (Role-based: `ADMIN`, `STAFF`)
- **Testing & Mocking:** JUnit 5, Mockito, Spring Test / MockMvc
- **Browser Automation:** Selenium WebDriver 4 (Headless Chrome) with Page Object Model (POM)
- **Coverage Analysis:** JaCoCo 0.8.12
- **Continuous Integration:** GitHub Actions, Jenkins Pipeline, Docker Compose

---

## Business Rules & Specifications

- **Mark Validation:** Valid marks are integers strictly between 0 and 100 (inclusive).
- **Grading Scale:**
  - `90 - 100`: **A+**
  - `85 - 89`: **A**
  - `80 - 84`: **A-**
  - `75 - 79`: **B+**
  - `70 - 74`: **B**
  - `65 - 69`: **B-**
  - `60 - 64`: **C+**
  - `50 - 59`: **C**
  - `40 - 49`: **D**
  - `0 - 39`: **F**
- **State Machine Workflow:** `DRAFT` $\rightarrow$ `SUBMITTED` $\rightarrow$ `REVIEWED` $\rightarrow$ `PUBLISHED`.
- **Correction Loop:** `SUBMITTED` $\rightarrow$ `DRAFT`.
- **Publication Guard (Decision Table):** Publishing requires 3 simultaneous conditions: valid mark, reviewed status, and active student enrollment.
- **Privacy Enforcement:** Public search displays **only** results with `PUBLISHED` status.

---

## How to Run the Application

```
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in your browser:
- **Default Administrative Credentials:**
  - `admin` / `admin123` (Administrator)
  - `staff` / `staff123` (Department Staff)
- **Public Student Portal:** [http://localhost:8080/student-results](http://localhost:8080/student-results) (no login required)

---

## How to Run Automated Tests

### 1. Run Unit & Integration Tests (Surefire)

```
mvn test
```

### 2. Run Full Verification with Selenium System Tests & Coverage (Failsafe)

```
mvn clean verify -Psystem-tests
```

### 3. Inspect JaCoCo Coverage Report

After running verification, open the generated HTML report:

```text
target/site/jacoco/index.html
```

**Quality Gate Result:**
- Service Layer Branch Coverage: **96.3%** (Exceeds 80% threshold).
- Service Layer Line Coverage: **100.0%**.
- Total Codebase Branch Coverage: **91.9%**.

---

## Continuous Integration & Pipelines

### 1. GitHub Actions
Defined in `.github/workflows/ci.yml`. On every push and pull request, GitHub Actions:
- Provisions Ubuntu Latest with Java 17 and Chrome.
- Runs `mvn clean -Psystem-tests verify`.
- Enforces the 80% branch coverage quality check.
- Archives and uploads the JaCoCo report as a build artifact.

### 2. Jenkins Pipeline & Docker Setup
Defined in `Jenkinsfile` as a 7-stage declarative pipeline:
1. `Checkout`: Checks out branch from Git.
2. `Build`: Compiles code (`mvn clean package -DskipTests`).
3. `Unit Tests`: Executes unit test suite (`mvn test`).
4. `Integration Tests`: Verifies repository and MVC integration.
5. `Coverage`: Executes `jacoco:report` and `jacoco:check`.
6. `System Tests`: Runs Selenium tests against headless browser (`mvn verify -Psystem-tests`).
7. `Archive Results`: Records JUnit XML reports and archives HTML coverage.

To run Jenkins locally via Docker:

```
docker compose -f docker-compose.jenkins.yml up --build

```
## Regression Testing Demonstration

To demonstrate regression detection in accordance with course requirements:
1. **Original Passing Code:** `mark >= 50` in `GradeService.java` correctly returns `"C"`.
2. **Injected Regression Fault:** Alter operator to `mark > 50`.
3. **Regression Detection:** `GradeServiceTest.java` immediately fails on boundary input `50` (returns `"D"` instead of `"C"`). `ResultServiceTest` and `StudentResultSystemTest` also fail.
4. **Fix & Verification:** Relational operator restored to `>= 50`, returning pipeline to green.


