package com.educhain.service;

import com.educhain.dto.request.CourseRequest;
import com.educhain.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    // Create Course
    CourseResponse saveCourse(CourseRequest request);

    // Get All Courses
    List<CourseResponse> getAllCourses();

    // Pagination
    Page<CourseResponse> getAllCourses(Pageable pageable);

    // Search Course by Name
    List<CourseResponse> searchCourseByName(String courseName);

    // Search Course by Department
    List<CourseResponse> getCoursesByDepartment(String department);

    // Get Course By Id
    CourseResponse getCourseById(Long id);

    // Update Course
    CourseResponse updateCourse(Long id, CourseRequest request);

    // Delete Course
    void deleteCourse(Long id);
}