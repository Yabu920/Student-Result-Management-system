package com.example.studentresult.integration;

import com.example.studentresult.model.Course;
import com.example.studentresult.model.Student;
import com.example.studentresult.repository.CourseRepository;
import com.example.studentresult.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ResultControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @WithMockUser
    void resultEntryPageLoads() throws Exception {
        mockMvc.perform(get("/results"))
                .andExpect(status().isOk())
                .andExpect(view().name("results"));
    }

    @Test
    @WithMockUser
    void postValidResultRedirectsToResultsPage() throws Exception {
        Student student = studentRepository.save(new Student("STU-400", "Controller Student", true));
        Course course = courseRepository.save(new Course("SE504", "Controller Testing", 3));

        mockMvc.perform(post("/results")
                        .with(csrf())
                        .param("studentId", student.getId().toString())
                        .param("courseId", course.getId().toString())
                        .param("mark", "75"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/results"));
    }
}
