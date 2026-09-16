package com.example.studentresult.service;

import com.example.studentresult.model.ResultStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkflowServiceTest {

    private final WorkflowService workflowService = new WorkflowService();

    @Test
    void acceptsValidWorkflowTransitions() {
        assertTrue(workflowService.canTransition(ResultStatus.DRAFT, ResultStatus.SUBMITTED));
        assertTrue(workflowService.canTransition(ResultStatus.SUBMITTED, ResultStatus.DRAFT));
        assertTrue(workflowService.canTransition(ResultStatus.SUBMITTED, ResultStatus.REVIEWED));
        assertTrue(workflowService.canTransition(ResultStatus.REVIEWED, ResultStatus.PUBLISHED));
    }

    @Test
    void rejectsInvalidWorkflowTransitions() {
        assertFalse(workflowService.canTransition(ResultStatus.DRAFT, ResultStatus.PUBLISHED));
        assertFalse(workflowService.canTransition(ResultStatus.PUBLISHED, ResultStatus.DRAFT));
        assertFalse(workflowService.canTransition(ResultStatus.REVIEWED, ResultStatus.DRAFT));
    }
}
