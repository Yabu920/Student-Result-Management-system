package com.example.studentresult.controller;

import com.example.studentresult.model.ResultStatus;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.StudentRepository;
import com.example.studentresult.service.ResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public ResultController(ResultService resultService, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.resultService = resultService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String entry(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("results", resultService.findAll());
        return "results";
    }

    @PostMapping
    public String create(@RequestParam Long studentId,
                         @RequestParam Long courseId,
                         @RequestParam Integer mark,
                         RedirectAttributes redirectAttributes) {
        try {
            resultService.createDraft(studentId, courseId, mark);
            redirectAttributes.addFlashAttribute("message", "Result saved as draft");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/results";
    }

    @PostMapping("/status")
    public String changeStatus(@RequestParam Long resultId,
                               @RequestParam ResultStatus targetStatus,
                               RedirectAttributes redirectAttributes) {
        try {
            resultService.changeStatus(resultId, targetStatus);
            redirectAttributes.addFlashAttribute("message", "Result status updated");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/results";
    }
}
