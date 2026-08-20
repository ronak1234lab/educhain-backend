package com.educhain.service;

import com.educhain.dto.request.EnrollmentRequest;
import com.educhain.dto.response.EnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnrollmentService {

    // Create Enrollment
    EnrollmentResponse saveEnrollment(EnrollmentRequest request);

    // Get All Enrollments
    List<EnrollmentResponse> getAllEnrollments();

    // Pagination
    Page<EnrollmentResponse> getAllEnrollments(Pageable pageable);

    // Get Enrollment By Id
    EnrollmentResponse getEnrollmentById(Long id);

    // Get All Courses of a Student
    List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId);

    // Get All Students of a Course
    List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId);

    // Delete Enrollment
    void deleteEnrollment(Long id);
}