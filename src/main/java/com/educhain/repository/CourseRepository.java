package com.educhain.repository;

import com.educhain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    // ==========================================
    // Search by Course Name
    // ==========================================

    List<Course> findByCourseNameContainingIgnoreCase(
            String courseName
    );


    // ==========================================
    // Duplicate Course Code - Create
    // ==========================================

    boolean existsByCourseCode(
            String courseCode
    );


    // ==========================================
    // Duplicate Course Code - Update
    // ==========================================

    boolean existsByCourseCodeAndIdNot(
            String courseCode,
            Long id
    );


    // ==========================================
    // Find Courses by Department
    // ==========================================

    List<Course> findByDepartmentIgnoreCase(
            String department
    );
}