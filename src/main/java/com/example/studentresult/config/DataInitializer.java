package com.example.studentresult.config;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Student;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(StudentRepository studentRepository, CourseRepository courseRepository) {
        return args -> {
            if (studentRepository.count() == 0) {
                studentRepository.save(new Student("STU-001", "Amina Tesfaye", true));
                studentRepository.save(new Student("STU-002", "Daniel Bekele", true));
                studentRepository.save(new Student("STU-003", "Sara Abebe", false));
            }
            if (courseRepository.count() == 0) {
                courseRepository.save(new Course("SE501", "Software Testing and Validation", 3));
                courseRepository.save(new Course("SE502", "Software Project Management", 3));
            }
        };
    }
}
