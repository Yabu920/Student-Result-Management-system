package com.example.studentresult.controller;

import com.example.studentresult.service.ResultService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentResultController {

    private final ResultService resultService;

    public StudentResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/student-results")
    public String studentResults(@RequestParam(required = false) String studentNumber, Model model) {
        model.addAttribute("studentNumber", studentNumber);
        if (studentNumber != null && !studentNumber.isBlank()) {
            model.addAttribute("results", resultService.findPublishedForStudent(studentNumber));
        }
        return "student-results";
    }
}
