package com.example.studentresult.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GradeServiceTest {

    private final GradeService gradeService = new GradeService(new MarkValidator());

    @ParameterizedTest
    @CsvSource({
            "0,F", "1,F", "39,F",
            "40,D", "41,D", "49,D",
            "50,C", "51,C", "59,C",
            "60,C+", "61,C+", "64,C+",
            "65,B-", "66,B-", "69,B-",
            "70,B", "71,B", "74,B",
            "75,B+", "76,B+", "79,B+",
            "80,A-", "81,A-", "84,A-",
            "85,A", "86,A", "89,A",
            "90,A+", "91,A+", "99,A+", "100,A+"
    })
    void calculateGradeReturnsExpectedGradeForBoundaries(int mark, String expectedGrade) {
        assertEquals(expectedGrade, gradeService.calculateGrade(mark));
    }

    @Test
    void calculateGradeRejectsInvalidLowMark() {
        assertThrows(IllegalArgumentException.class, () -> gradeService.calculateGrade(-1));
    }

    @Test
    void calculateGradeRejectsInvalidHighMark() {
        assertThrows(IllegalArgumentException.class, () -> gradeService.calculateGrade(101));
    }
}
