package com.example.studentresult.controller;

import com.example.studentresult.model.Course;
import com.example.studentresult.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("course", new Course("", "", 3));
        return "courses";
    }

    @PostMapping
    public String create(Course course) {
        courseRepository.save(course);
        return "redirect:/courses";
    }
}
