package com.example.studentresult.service;

import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import org.springframework.stereotype.Service;

@Service
public class PublicationService {

    private final MarkValidator markValidator;

    public PublicationService(MarkValidator markValidator) {
        this.markValidator = markValidator;
    }

    public boolean canPublish(Result result) {
        return markValidator.isValid(result.getMark())
                && result.getStatus() == ResultStatus.REVIEWED
                && result.getStudent().isActive();
    }

    public void requirePublishable(Result result) {
        if (!canPublish(result)) {
            throw new IllegalStateException("Result cannot be published until mark is valid, result is reviewed, and student is active");
        }
    }
}
