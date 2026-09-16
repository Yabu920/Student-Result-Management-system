package com.example.studentresult.service;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import com.example.studentresult.model.Student;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.ResultRepository;
import com.example.studentresult.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final MarkValidator markValidator;
    private final GradeService gradeService;
    private final WorkflowService workflowService;
    private final PublicationService publicationService;

    public ResultService(ResultRepository resultRepository,
                         StudentRepository studentRepository,
                         CourseRepository courseRepository,
                         MarkValidator markValidator,
                         GradeService gradeService,
                         WorkflowService workflowService,
                         PublicationService publicationService) {
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.markValidator = markValidator;
        this.gradeService = gradeService;
        this.workflowService = workflowService;
        this.publicationService = publicationService;
    }

    @Transactional
    public Result createDraft(Long studentId, Long courseId, Integer mark) {
        markValidator.requireValid(mark);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Result result = new Result(student, course, mark);
        result.setGrade(gradeService.calculateGrade(mark));
        result.setStatus(ResultStatus.DRAFT);
        return resultRepository.save(result);
    }

    @Transactional
    public Result changeStatus(Long resultId, ResultStatus targetStatus) {
        Result result = findById(resultId);
        workflowService.requireTransition(result.getStatus(), targetStatus);
        if (targetStatus == ResultStatus.PUBLISHED) {
            publicationService.requirePublishable(result);
        }
        result.setStatus(targetStatus);
        return resultRepository.save(result);
    }

    public Result findById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Result not found"));
    }

    public List<Result> findAll() {
        return resultRepository.findAll();
    }

    public List<Result> findPublishedForStudent(String studentNumber) {
        return resultRepository.findByStudentStudentNumberAndStatus(studentNumber, ResultStatus.PUBLISHED);
    }
}
