package com.example.studentresult.service;

import org.springframework.stereotype.Service;

@Service
public class GradeService {

    private final MarkValidator markValidator;

    public GradeService(MarkValidator markValidator) {
        this.markValidator = markValidator;
    }

    public String calculateGrade(Integer mark) {
        markValidator.requireValid(mark);
        if (mark >= 90) {
            return "A+";
        }
        if (mark >= 85) {
            return "A";
        }
        if (mark >= 80) {
            return "A-";
        }
        if (mark >= 75) {
            return "B+";
        }
        if (mark >= 70) {
            return "B";
        }
        if (mark >= 65) {
            return "B-";
        }
        if (mark >= 60) {
            return "C+";
        }
        if (mark >= 50) {
            return "C";
        }
        if (mark >= 40) {
            return "D";
        }
        return "F";
    }
}
