# Defect Log and Metrics

## 1. Defect Lifecycle

The project follows a standard defect tracking workflow:

```text
NEW -> ASSIGNED -> IN PROGRESS -> FIXED -> RETEST -> CLOSED
                                            |
                                            v (if retest fails)
                                         REOPENED
```

- **NEW:** Defect detected during test execution and logged.
- **ASSIGNED:** Defect routed to the developer responsible for the component.
- **IN PROGRESS:** Fix under development and local verification.
- **FIXED:** Patch committed to branch.
- **RETEST:** QA re-executes failing test cases and regression suites.
- **CLOSED:** Verified as resolved and merged into baseline.
- **REOPENED:** Retest failed; returned to developer.

---

## 2. Defect Log

| ID | Title | Description | Steps to Reproduce | Expected Result | Actual Result | Severity | Priority | Status | Reporter | Assignee | Opened | Fixed | Retest |
|---|---|---|---|---|---|:---:|:---:|:---:|---|---|---|---|---|
| **DEF-001** | Grade boundary off-by-one for mark 50 | Entering boundary mark 50 generated Grade D instead of Grade C. | 1. Invoke `GradeService.calculateGrade(50)`<br>2. Check returned letter grade. | Expected Grade: `"C"` | Received Grade: `"D"` | High | High | **CLOSED** | QA Engineer | Backend Dev | 2026-09-10 | 2026-09-11 | 2026-09-11 |
| **DEF-002** | Publication rule allows inactive students | Publication guard only checked review status, omitting active student verification. | 1. Create result for inactive student `STU-003`<br>2. Transition to REVIEWED<br>3. Attempt transition to PUBLISHED. | System throws `IllegalStateException` preventing publication. | Result transitioned to PUBLISHED status. | High | High | **CLOSED** | QA Engineer | Backend Dev | 2026-09-11 | 2026-09-12 | 2026-09-12 |
| **DEF-003** | Missing workflow state transition validation | UI and controller allowed direct transition from DRAFT to PUBLISHED without intermediate review. | 1. Create result in DRAFT status<br>2. Directly send target status `PUBLISHED` via `/results/status`. | Transition rejected with workflow exception; status remains DRAFT. | Result immediately marked as PUBLISHED without audit review. | Medium | High | **CLOSED** | QA Engineer | Backend Dev | 2026-09-12 | 2026-09-13 | 2026-09-13 |

---

## 3. Quality Metrics & Interpretations

The table below summarizes the key quality metrics computed for the project, following the requirements in Part G of the project specification.

| Metric | Formula | Computed Value | Interpretation |
|---|---|:---:|---|
| **Defects Found** | Count of defects identified across all test tiers | **3** | All 3 defects were identified during early testing (unit and integration levels) prior to release sign-off. |
| **Escaped Defects** | Defects discovered after acceptance testing / in production | **0** | Zero defects escaped into the final acceptance and system test runs, indicating high verification effectiveness. |
| **Total Lines of Code (KLOC)** | Total Java source lines in `src/main/java` / 1,000 | **0.708 KLOC** (708 LOC) | The codebase is compact and modular, keeping architectural complexity low and testability high. |
| **Defect Density** | $\frac{\text{Defects Found}}{\text{KLOC}}$ | **4.24 defects / KLOC** | A density of 4.24 defects per KLOC is well within industry norms for academic and small-scale software projects (typically 2 to 10 defects/KLOC). |
| **Defect Removal Efficiency (DRE)** | $\frac{\text{Defects Removed Before Release}}{\text{Total Defects Found}} \times 100\%$ | **100.0%** | Since all 3 identified defects were resolved and verified before acceptance testing, the DRE is 100%, demonstrating that no known faults remain unaddressed. |
| **Statement (Line) Coverage** | $\frac{\text{Lines Covered}}{\text{Total Executable Lines}} \times 100\%$ | **83.8%** (186 / 222 lines) | Comprehensive test execution traverses 83.8% of all application lines, with core business logic reaching 100% line coverage. |
| **Branch Coverage (Core Service Logic)** | $\frac{\text{Branches Covered in Services}}{\text{Total Service Branches}} \times 100\%$ | **96.3%** (52 / 54 branches) | Far exceeds the course threshold of 80% branch coverage on business logic, ensuring virtually all decision outcomes are validated. |
| **Total Branch Coverage** | $\frac{\text{Total Branches Covered}}{\text{Total Application Branches}} \times 100\%$ | **91.9%** (57 / 62 branches) | Confirms that control flow branches across services, controllers, and configurations are thoroughly tested. |
