package com.educhain.repository;

import com.educhain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long> {

    // ==========================================
    // Search Student By Name
    // ==========================================

    List<Student> findByStudentNameContainingIgnoreCase(
            String studentName
    );


    // ==========================================
    // Check Duplicate Email
    // ==========================================

    boolean existsByEmail(String email);


    // ==========================================
    // Check Duplicate Email During Update
    // ==========================================

    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );


    // ==========================================
    // Check Duplicate Phone
    // ==========================================

    boolean existsByPhone(String phone);


    // ==========================================
    // Check Duplicate Phone During Update
    // ==========================================

    boolean existsByPhoneAndIdNot(
            String phone,
            Long id
    );


    // ==========================================
    // Check Duplicate Enrollment Number
    // ==========================================

    boolean existsByEnrollmentNumber(
            String enrollmentNumber
    );


    // ==========================================
    // Check Duplicate Enrollment During Update
    // ==========================================

    boolean existsByEnrollmentNumberAndIdNot(
            String enrollmentNumber,
            Long id
    );
}