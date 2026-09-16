# Test Plan: Student Result Management and Publication System

## 1. Document & Team Information

- **Course:** Software Testing and Validation (Addis Ababa University)
- **Instructor:** Abel Tadesse
- **Project:** Final Project: A Complete Testing Effort
- **Date:** September 2026

### Group Members & Roles

| Member Name | Student ID | Primary Role | Assigned Responsibilities |
|---|---|---|---|
| **Member 1** | ATR/0001/14 | **Test Lead & QA Architect** | Test planning, risk prioritization, defect management, foundations reflection. |
| **Member 2** | ATR/0002/14 | **Unit & Integration Test Engineer** | Test design (EP, BVA, Decision Table, State Machine), unit test suites, Mockito test doubles. |
| **Member 3** | ATR/0003/14 | **Test Automation Engineer** | Selenium WebDriver end-to-end testing, Page Object Model (POM) design, cross-browser scenarios. |
| **Member 4** | ATR/0004/14 | **DevOps & CI/CD Engineer** | GitHub Actions workflow, Jenkins pipeline, Docker containerization, regression testing automation. |

*(Note: Replace placeholder IDs with your group's official student IDs prior to final submission).*

---

## 2. Scope

The testing effort covers all critical functions of the Student Result Management and Publication System:
- **In Scope:**
  - Administrative authentication and role-based access control (`ADMIN`, `STAFF`).
  - Student enrollment management and active/inactive status flag tracking.
  - Course curriculum catalog definitions (course code, title, credit hours).
  - Numerical mark entry and range boundary validation ($0 \le \text{mark} \le 100$).
  - Automated letter grade derivation across all 10 grade bands (`A+` down to `F`).
  - Multi-condition publication eligibility rules (Decision Table verification).
  - State machine lifecycle transitions (`DRAFT` $\rightarrow$ `SUBMITTED` $\rightarrow$ `REVIEWED` $\rightarrow$ `PUBLISHED`, and correction loops).
  - Database filtering and student privacy isolation (only `PUBLISHED` results visible).
  - Code coverage measurement ($\ge 80\%$ branch coverage on core business logic).
- **Out of Scope:**
  - Performance load testing and high concurrency stress testing.
  - External single sign-on (SSO/LDAP) enterprise integrations.

---

## 3. Approach (Levels and Techniques)

Testing follows the **Test Pyramid** principle, with many fast unit tests, fewer integration tests, and a focused suite of system tests:

1. **Unit Testing (JUnit 5, Mockito):**
   - Focus: Business services in isolation (`MarkValidator`, `GradeService`, `PublicationService`, `WorkflowService`, `ResultService`).
   - Techniques: Equivalence Partitioning, Boundary Value Analysis, Decision Table Testing, State Transition Testing.
   - Isolation: Mockito test doubles (`mock()`, `when()`, `verify()`, `never()`) to isolate services from data repositories.

2. **Integration Testing (Spring Boot Test, Spring Data JPA, MockMvc):**
   - Focus: Component collaboration, database query correctness with in-memory H2, Spring MVC HTTP endpoints, and Spring Security filters.

3. **System Testing (Selenium WebDriver 4, Headless Chrome):**
   - Focus: Full user journeys through the web UI using the Page Object Model (POM).
   - Scenarios: Login/logout, mark entry, status workflow transitions, student public search.

4. **Code Coverage (JaCoCo):**
   - Enforced via Maven build quality gate with a minimum 80% branch coverage threshold.

---

## 4. Entry and Exit Criteria

### Entry Criteria
- System requirements and grading boundary specifications are documented.
- Application compiles and starts successfully.
- Test data (students and courses) seeded via `DataInitializer`.
- Test environment (Java 17, Maven, Chrome WebDriver) initialized.

### Exit Criteria
- **Zero open Critical or High-severity defects.**
- **Minimum 80% branch coverage** achieved for core business services (`com.example.studentresult.service`).
- **100% pass rate** across all automated unit, integration, and system test suites.
- GitHub Actions CI pipeline and Jenkins pipeline successfully build, test, and package.
- Deliberate regression demonstration executed, detected by test suite, and recorded.
- Acceptance testing scenarios completed and signed off.

---

## 5. Risk-Based Prioritization

Testing effort is allocated based on business criticality and failure impact:

| Functional Area | Risk Level | Rationale | Testing Priority & Technique |
|---|:---:|---|---|
| **Grade Calculation** | **Critical** | An error in calculating grades directly damages academic integrity and student records. | Highest: Comprehensive BVA on all 31 boundary values. |
| **Publication Rules** | **High** | Premature or improper release of unreviewed/inactive marks breaches institutional policy. | Highest: Full Decision Table ($2^3=8$ combinations) & guard assertions. |
| **Workflow State Machine** | **High** | Skipping review steps or modifying published marks corrupts historical audit trails. | High: State transition testing of valid and invalid paths. |
| **Authentication & Access** | **High** | Unauthorized access could allow tampering with student marks. | High: Spring Security integration and login UI scenarios. |
| **Database Persistence** | **Medium** | Improper query filtering could expose unapproved drafts to students. | Medium: Spring Data JPA repository query tests. |
| **UI Aesthetics & Layout** | **Low** | Layout issues affect user experience but do not invalidate academic results. | Normal: Page Object Model UI inspection and verification. |

---

## 6. Project Schedule (3-Week Plan)

- **Week 1: Foundations & Planning**
  - Define application requirements and build initial working software.
  - Formulate Test Plan (Part A) and derive test cases in Test Design Document (Part B).
- **Week 2: Test Implementation & Automation**
  - Implement unit test suites (EP, BVA, Decision Table, State Machine).
  - Implement integration tests with MockMvc and JPA.
  - Develop Selenium Page Object classes and system test suite.
  - Setup GitHub Actions workflow and Jenkins Docker pipeline; reach $\ge 80\%$ branch coverage.
- **Week 3: Quality Governance, Regression & Reporting**
  - Execute deliberate regression demonstration (`mark >= 50` vs `mark > 50`).
  - Maintain Defect Log and compute metrics (Defect Density, DRE).
  - Compile final Test Summary Report and Foundations Reflection.

---

## 7. Current Execution Status

- **Status:** **COMPLETED & VERIFIED**
- **Test Results:** **70 Passed / 0 Failed / 0 Skipped** (100% pass rate).
- **Branch Coverage:** **96.3%** in core services (Target: $\ge 80\%$).
- **Defects:** 3 defects logged, investigated, resolved, retested, and **CLOSED**.
- **Quality Gate:** **PASSED**.
