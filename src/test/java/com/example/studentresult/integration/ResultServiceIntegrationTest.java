package com.example.studentresult.integration;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import com.example.studentresult.model.Student;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.StudentRepository;
import com.example.studentresult.service.ResultService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResultServiceIntegrationTest {

    @Autowired
    private ResultService resultService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void serviceCreatesDraftWithRealRepositories() {
        Student student = studentRepository.save(new Student("STU-300", "Integration Student", true));
        Course course = courseRepository.save(new Course("SE503", "Integration Testing", 3));

        Result result = resultService.createDraft(student.getId(), course.getId(), 90);

        assertEquals("A+", result.getGrade());
        assertEquals(ResultStatus.DRAFT, result.getStatus());
    }
}
