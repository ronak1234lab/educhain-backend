package com.educhain.controller;

import com.educhain.dto.request.StudentRequest;
import com.educhain.dto.response.StudentResponse;
import com.educhain.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Create Student
    @PostMapping
    public StudentResponse createStudent(@Valid @RequestBody StudentRequest request) {
        return studentService.saveStudent(request);
    }

    // Get All Students
    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Pagination + Sorting
    @GetMapping("/page")
    public Page<StudentResponse> getAllStudentsWithPagination(
            @PageableDefault(size = 5, sort = "studentName") Pageable pageable) {

        return studentService.getAllStudents(pageable);
    }

    // Search Student
    @GetMapping("/search")
    public List<StudentResponse> searchStudentByName(
            @RequestParam String name) {

        return studentService.searchStudentByName(name);
    }

    // Get Student By Id
    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // Update Student
    @PutMapping("/{id}")
    public StudentResponse updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        return studentService.updateStudent(id, request);
    }

    // Delete Student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);
        return "Student deleted successfully!";
    }
}