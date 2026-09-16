package com.example.studentresult.service;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import com.example.studentresult.model.Student;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PublicationServiceTest {

    private final PublicationService publicationService = new PublicationService(new MarkValidator());

    @ParameterizedTest
    @CsvSource({
            "true,true,true,true",
            "true,true,false,false",
            "true,false,true,false",
            "true,false,false,false",
            "false,true,true,false",
            "false,true,false,false",
            "false,false,true,false",
            "false,false,false,false"
    })
    void canPublishUsesDecisionTable(boolean validMark, boolean reviewed, boolean activeStudent, boolean expected) {
        Student student = new Student("STU-100", "Decision Table Student", activeStudent);
        Course course = new Course("SE501", "Testing", 3);
        Result result = new Result(student, course, validMark ? 70 : 101);
        result.setStatus(reviewed ? ResultStatus.REVIEWED : ResultStatus.SUBMITTED);

        assertEquals(expected, publicationService.canPublish(result));
    }

    @ParameterizedTest
    @CsvSource({
            "70,REVIEWED,true,true",
            "101,REVIEWED,true,false",
            "70,SUBMITTED,true,false",
            "70,REVIEWED,false,false"
    })
    void requirePublishableAcceptsOnlyPublishableResults(int mark, ResultStatus status, boolean activeStudent, boolean expectedAllowed) {
        Student student = new Student("STU-101", "Publication Guard Student", activeStudent);
        Course course = new Course("SE501", "Testing", 3);
        Result result = new Result(student, course, mark);
        result.setStatus(status);

        if (expectedAllowed) {
            assertDoesNotThrow(() -> publicationService.requirePublishable(result));
        } else {
            assertThrows(IllegalStateException.class, () -> publicationService.requirePublishable(result));
        }
    }
}
