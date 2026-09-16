package com.example.studentresult.service;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import com.example.studentresult.model.Student;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.ResultRepository;
import com.example.studentresult.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResultServiceTest {

    @Test
    void createDraftUsesRepositoriesAndCalculatesGrade() {
        ResultRepository resultRepository = mock(ResultRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        Student student = new Student("STU-001", "Amina Tesfaye", true);
        Course course = new Course("SE501", "Testing", 3);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultService resultService = new ResultService(
                resultRepository,
                studentRepository,
                courseRepository,
                new MarkValidator(),
                new GradeService(new MarkValidator()),
                new WorkflowService(),
                new PublicationService(new MarkValidator())
        );

        Result saved = resultService.createDraft(1L, 2L, 50);

        assertEquals("C", saved.getGrade());
        assertEquals(ResultStatus.DRAFT, saved.getStatus());
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(2L);
        verify(resultRepository).save(any(Result.class));
    }

    @Test
    void createDraftRejectsInvalidMarkBeforeRepositoryLookup() {
        ResultRepository resultRepository = mock(ResultRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        ResultService resultService = resultService(resultRepository, studentRepository, courseRepository);

        assertThrows(IllegalArgumentException.class, () -> resultService.createDraft(1L, 2L, 101));

        verify(studentRepository, never()).findById(any());
        verify(courseRepository, never()).findById(any());
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    void createDraftRejectsMissingStudent() {
        ResultRepository resultRepository = mock(ResultRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        ResultService resultService = resultService(resultRepository, studentRepository, courseRepository);

        assertThrows(IllegalArgumentException.class, () -> resultService.createDraft(99L, 2L, 70));

        verify(courseRepository, never()).findById(any());
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    void createDraftRejectsMissingCourse() {
        ResultRepository resultRepository = mock(ResultRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student("STU-001", "Amina Tesfaye", true)));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        ResultService resultService = resultService(resultRepository, studentRepository, courseRepository);

        assertThrows(IllegalArgumentException.class, () -> resultService.createDraft(1L, 99L, 70));

        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    void changeStatusRejectsInvalidTransitionAndDoesNotSave() {
        ResultRepository resultRepository = mock(ResultRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        Result result = new Result(
                new Student("STU-001", "Amina Tesfaye", true),
                new Course("SE501", "Testing", 3),
                80
        );
        result.setStatus(ResultStatus.DRAFT);
        when(resultRepository.findById(5L)).thenReturn(Optional.of(result));
        ResultService resultService = resultService(resultRepository, studentRepository, courseRepository);

        assertThrows(IllegalStateException.class, () -> resultService.changeStatus(5L, ResultStatus.PUBLISHED));

        assertEquals(ResultStatus.DRAFT, result.getStatus());
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    void changeStatusRejectsMissingResult() {
        ResultRepository resultRepository = mock(ResultRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        when(resultRepository.findById(404L)).thenReturn(Optional.empty());
        ResultService resultService = resultService(resultRepository, studentRepository, courseRepository);

        assertThrows(IllegalArgumentException.class, () -> resultService.changeStatus(404L, ResultStatus.SUBMITTED));

        verify(resultRepository, never()).save(any(Result.class));
    }

    private ResultService resultService(ResultRepository resultRepository,
                                        StudentRepository studentRepository,
                                        CourseRepository courseRepository) {
        return new ResultService(
                resultRepository,
                studentRepository,
                courseRepository,
                new MarkValidator(),
                new GradeService(new MarkValidator()),
                new WorkflowService(),
                new PublicationService(new MarkValidator())
        );
    }
}
