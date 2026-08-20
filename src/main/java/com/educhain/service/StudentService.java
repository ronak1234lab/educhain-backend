package com.educhain.service;

import com.educhain.dto.request.StudentRequest;
import com.educhain.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    // Create Student
    StudentResponse saveStudent(StudentRequest request);

    // Get All Students
    List<StudentResponse> getAllStudents();

    // Pagination
    Page<StudentResponse> getAllStudents(Pageable pageable);

    // Search Student
    List<StudentResponse> searchStudentByName(String studentName);

    // Get Student By Id
    StudentResponse getStudentById(Long id);

    // Update Student
    StudentResponse updateStudent(Long id, StudentRequest request);

    // Delete Student
    void deleteStudent(Long id);
}
