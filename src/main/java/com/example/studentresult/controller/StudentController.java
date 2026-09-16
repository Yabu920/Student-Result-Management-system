package com.example.studentresult.controller;

import com.example.studentresult.model.Student;
import com.example.studentresult.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("student", new Student("", "", true));
        return "students";
    }

    @PostMapping
    public String create(Student student) {
        studentRepository.save(student);
        return "redirect:/students";
    }
}
