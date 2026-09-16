package com.example.studentresult.service;

import com.example.studentresult.model.ResultStatus;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

    public boolean canTransition(ResultStatus current, ResultStatus target) {
        return (current == ResultStatus.DRAFT && target == ResultStatus.SUBMITTED)
                || (current == ResultStatus.SUBMITTED && target == ResultStatus.DRAFT)
                || (current == ResultStatus.SUBMITTED && target == ResultStatus.REVIEWED)
                || (current == ResultStatus.REVIEWED && target == ResultStatus.PUBLISHED);
    }

    public void requireTransition(ResultStatus current, ResultStatus target) {
        if (!canTransition(current, target)) {
            throw new IllegalStateException("Invalid result workflow transition: " + current + " to " + target);
        }
    }
}
