# Test Summary Report and Foundations Reflection

## 1. Test Summary Report

### 1.1 Scope of Testing
The testing effort covered all functional and non-functional requirements of the Student Result Management and Publication System across three distinct tiers:

- **What Was Tested:**
  - Mark range validation ($0 \le \text{mark} \le 100$) and boundary values.
  - Complete 10-tier grade calculation scale (`A+`, `A`, `A-`, `B+`, `B`, `B-`, `C+`, `C`, `D`, `F`).
  - Decision table rules for publication eligibility (valid mark, reviewed status, active student).
  - Workflow state machine transitions (`DRAFT` $\rightarrow$ `SUBMITTED` $\rightarrow$ `REVIEWED` $\rightarrow$ `PUBLISHED`, and correction loops).
  - Data persistence, filtering, and student view isolation using Spring Data JPA.
  - End-to-end user journeys using Selenium WebDriver (login, mark entry, status transitions, public student lookup).
  - Code branch coverage using JaCoCo.

- **What Was Not Tested (Out of Scope / Residual Risk):**
  - Performance and high-concurrency load testing (application is an academic demonstration with in-memory H2).
  - Integration with external enterprise identity providers (LDAP/OAuth); in-memory credentials used.
  - Cross-browser compatibility across legacy browsers (tested specifically on modern Chromium / Headless Chrome).

---

### 1.2 Results Against Exit Criteria

| Exit Criterion | Target Defined in Test Plan | Actual Result Achieved | Evaluation |
|---|---|---|:---:|
| **Critical / High Defects** | 0 open Critical or High defects | 0 open defects (all 3 resolved and closed) | **MET** |
| **Branch Coverage (Core Services)** | $\ge 80.0\%$ | **96.3%** (52 / 54 branches) | **MET** |
| **Total Test Pass Rate** | 100% pass rate | **100%** (70 / 70 tests passed) | **MET** |
| **Selenium End-to-End Scenarios** | Critical journeys pass in supported browser | 8 / 8 system tests passed | **MET** |
| **Automated CI Pipelines** | Working GitHub Actions & Jenkins pipelines | Configured, reproducible via Docker & Maven | **MET** |
| **Regression Demonstration** | Verified regression injection & detection | Boundary regression caught and resolved | **MET** |

---

### 1.3 User Acceptance Testing (UAT) Scenarios

| Scenario ID | Scenario Description | Expected Outcome | Status |
|:---:|---|---|:---:|
| **UAT-01** | Staff enters valid result for active student | Result saved as DRAFT with automatically computed grade. | **Passed** |
| **UAT-02** | Department reviewer reviews submitted mark | Status changes from SUBMITTED to REVIEWED. | **Passed** |
| **UAT-03** | Registrar publishes reviewed result | Status changes from REVIEWED to PUBLISHED. | **Passed** |
| **UAT-04** | Student views published result without authentication | Published course, mark, and grade are visible on public portal. | **Passed** |
| **UAT-05** | Student attempts to view unpublished draft result | Student search returns no records; unreleased marks are hidden. | **Passed** |

---

### 1.4 Outstanding Defects, Residual Risk & Release Recommendation

- **Outstanding Defects:** **0**. All defects logged in the defect tracking system have reached `CLOSED` state after successful retest.
- **Residual Risk:** **Minimal**. Minor residual risk pertains to production database sizing and external auth integration, which are well outside the scope of this coursework demonstration.
- **Final Release Recommendation:** **RECOMMENDED FOR RELEASE**. The system has fully passed all functional, integration, system, and coverage criteria.

---

## 2. Foundations Reflection: Error, Fault, Failure, Verification & Validation

In software testing engineering, understanding the foundational distinctions between **Human Error**, **Software Fault**, and **System Failure**, as well as the interplay between **Verification** and **Validation**, is paramount. During this project, defect **DEF-001** (Grade boundary condition for mark 50) provided a direct, real-world manifestation of these principles.

### Error, Fault, and Failure in DEF-001

1. **Human Error (The Mistake):**
   The developer committed a cognitive error during implementation. When reading the specification rule stating that *"a mark between 50 and 59 earns Grade C, while 40 to 49 earns Grade D"*, the developer mentally treated 50 as an exclusive boundary rather than an inclusive lower bound. This human mental lapse occurred before any code was compiled.

2. **Software Fault (The Bug in Code):**
   The human error manifested statically in the code artifact as an incorrect relational operator inside `GradeService.java`:
   ```java
   // FAULTY CODE (Static Fault):
   if (mark > 50) {
       return "C";
   }
   ```
   The fault lay quiescent in the codebase until an execution path exercised that exact boundary.

3. **System Failure (The Dynamic Manifestation):**
   When the test suite executed `calculateGrade(50)`, the execution traversed past the faulty condition into the next branch (`else if (mark >= 40)`), dynamically producing the return value `"D"`. The system failed to deliver its required service because the observable output (`"D"`) differed from the expected specification (`"C"`). This deviation constituted a system failure.

### Verification vs. Validation

- **Verification ("Are we building the product right?"):**
  Defect **DEF-001 was caught by Verification**. Automated boundary value unit tests (`GradeServiceTest.java`) verified the source code against the technical design specifications. Because unit tests isolated `GradeService` with parameterized inputs (`49, 50, 51`), the failure was exposed immediately during early developer testing without needing a running server, database, or UI. This prevented the fault from propagating into higher integration tiers.

- **Validation ("Are we building the right product?"):**
  Had verification failed to catch this bug, it would have persisted until **Validation**—either during Selenium end-to-end testing or during User Acceptance Testing by academic instructors simulating real grading workflows. Discovering this issue during validation would have required diagnosing complex layers (UI, controllers, service, database), dramatically increasing remediation cost. Catching it at the verification stage exemplifies the core principle of shift-left testing: detecting faults at the lowest possible level saves development time and guarantees software reliability.
