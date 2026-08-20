package com.educhain.controller;

import com.educhain.dto.request.EnrollmentRequest;
import com.educhain.dto.response.EnrollmentResponse;
import com.educhain.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Create Enrollment
    @PostMapping
    public EnrollmentResponse createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {

        return enrollmentService.saveEnrollment(request);
    }

    // Get All Enrollments
    @GetMapping
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    // Get Enrollment By Id
    @GetMapping("/{id}")
    public EnrollmentResponse getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    // Get All Courses of a Student
    @GetMapping("/student/{studentId}")
    public List<EnrollmentResponse> getEnrollmentsByStudent(
            @PathVariable Long studentId) {

        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    // Get All Students of a Course
    @GetMapping("/course/{courseId}")
    public List<EnrollmentResponse> getEnrollmentsByCourse(
            @PathVariable Long courseId) {

        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    // Pagination
    @GetMapping("/page")
    public Page<EnrollmentResponse> getEnrollmentsWithPagination(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        return enrollmentService.getAllEnrollments(pageable);
    }

    // Delete Enrollment
    @DeleteMapping("/{id}")
    public String deleteEnrollment(@PathVariable Long id) {

        enrollmentService.deleteEnrollment(id);
        return "Enrollment deleted successfully!";
    }
}