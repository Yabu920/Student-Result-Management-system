package com.example.studentresult.repository;

import com.example.studentresult.model.Result;
import com.example.studentresult.model.ResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentStudentNumberAndStatus(String studentNumber, ResultStatus status);
}
