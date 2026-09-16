package com.example.studentresult.service;

import org.springframework.stereotype.Service;

@Service
public class MarkValidator {

    public boolean isValid(Integer mark) {
        return mark != null && mark >= 0 && mark <= 100;
    }

    public void requireValid(Integer mark) {
        if (!isValid(mark)) {
            throw new IllegalArgumentException("Mark must be between 0 and 100");
        }
    }
}
