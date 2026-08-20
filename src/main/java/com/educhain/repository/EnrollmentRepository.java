package com.educhain.repository;

import com.educhain.entity.Course;
import com.educhain.entity.Enrollment;
import com.educhain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Check duplicate enrollment
    boolean existsByStudentAndCourse(Student student, Course course);

    // Get all enrollments of a student
    List<Enrollment> findByStudent(Student student);

    // Get all enrollments of a course
    List<Enrollment> findByCourse(Course course);
}