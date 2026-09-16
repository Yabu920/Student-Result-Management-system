# Test Design Document

## Equivalence Partitioning

| Partition | Example |
|---|---|
| Invalid low mark | -1 |
| Valid mark | 0, 50, 100 |
| Invalid high mark | 101 |

## Boundary Value Analysis

Boundary values: -1, 0, 1, 39, 40, 41, 49, 50, 51, 59, 60, 61, 64, 65, 66, 69, 70, 71, 74, 75, 76, 79, 80, 81, 84, 85, 86, 89, 90, 91, 99, 100, 101.

## Decision Table

| Valid Mark | Reviewed | Active Student | Can Publish |
|---|---|---|---|
| Yes | Yes | Yes | Yes |
| Yes | Yes | No | No |
| Yes | No | Yes | No |
| Yes | No | No | No |
| No | Yes | Yes | No |
| No | Yes | No | No |
| No | No | Yes | No |
| No | No | No | No |

## State Transition

Valid transitions:

- DRAFT to SUBMITTED
- SUBMITTED to DRAFT
- SUBMITTED to REVIEWED
- REVIEWED to PUBLISHED

Invalid examples:

- DRAFT to PUBLISHED
- PUBLISHED to DRAFT

## Requirement Traceability Matrix

| Requirement | Technique | Unit Test | Integration Test | System Test | Status |
|---|---|---|---|---|---|
| Mark validation | EP, BVA | MarkValidatorTest | ResultControllerIntegrationTest | StudentResultSystemTest | Implemented |
| Grade calculation | BVA | GradeServiceTest | ResultServiceIntegrationTest | StudentResultSystemTest | Implemented |
| Publication rule | Decision Table | PublicationServiceTest | To be completed | To be completed | Partly implemented |
| Workflow | State Transition | WorkflowServiceTest | To be completed | To be completed | Partly implemented |
| Login | System | To be completed | ResultControllerIntegrationTest | StudentResultSystemTest | Partly implemented |
