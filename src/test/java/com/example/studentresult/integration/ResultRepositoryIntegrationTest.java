package com.example.studentresult.integration;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import com.example.studentresult.model.Student;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.ResultRepository;
import com.example.studentresult.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ResultRepositoryIntegrationTest {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findsOnlyPublishedResultsForStudent() {
        Student student = studentRepository.save(new Student("STU-200", "Published Student", true));
        Course course = courseRepository.save(new Course("SE501", "Testing", 3));
        Result published = new Result(student, course, 80);
        published.setGrade("A-");
        published.setStatus(ResultStatus.PUBLISHED);
        resultRepository.save(published);

        Result draft = new Result(student, course, 90);
        draft.setGrade("A+");
        draft.setStatus(ResultStatus.DRAFT);
        resultRepository.save(draft);

        assertEquals(1, resultRepository.findByStudentStudentNumberAndStatus("STU-200", ResultStatus.PUBLISHED).size());
    }
}
