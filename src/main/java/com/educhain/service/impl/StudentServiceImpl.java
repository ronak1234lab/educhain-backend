package com.educhain.service.impl;

import com.educhain.dto.request.StudentRequest;
import com.educhain.dto.response.StudentResponse;
import com.educhain.entity.Student;
import com.educhain.entity.University;
import com.educhain.exception.ResourceNotFoundException;
import com.educhain.repository.StudentRepository;
import com.educhain.repository.UniversityRepository;
import com.educhain.service.StudentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl
        implements StudentService {

    private final StudentRepository studentRepository;

    private final UniversityRepository universityRepository;


    // ==========================================
    // Constructor
    // ==========================================

    public StudentServiceImpl(
            StudentRepository studentRepository,
            UniversityRepository universityRepository) {

        this.studentRepository =
                studentRepository;

        this.universityRepository =
                universityRepository;
    }


    // ==========================================
    // Create Student
    // ==========================================

    @Override
    public StudentResponse saveStudent(
            StudentRequest request) {


        // --------------------------------------
        // Duplicate Email
        // --------------------------------------

        if (studentRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists."
            );
        }


        // --------------------------------------
        // Duplicate Phone
        // --------------------------------------

        if (studentRepository.existsByPhone(
                request.getPhone())) {

            throw new RuntimeException(
                    "Phone number already exists."
            );
        }


        // --------------------------------------
        // Duplicate Enrollment Number
        // --------------------------------------

        if (studentRepository
                .existsByEnrollmentNumber(
                        request.getEnrollmentNumber())) {

            throw new RuntimeException(
                    "Enrollment number already exists."
            );
        }


        // --------------------------------------
        // Find University
        // --------------------------------------

        University university =
                universityRepository.findById(
                        request.getUniversityId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "University not found with id : "
                                        + request.getUniversityId()
                        )
                );


        // --------------------------------------
        // Create Student Entity
        // --------------------------------------

        Student student =
                new Student();


        student.setStudentName(
                request.getStudentName()
        );

        student.setEmail(
                request.getEmail()
        );

        student.setPhone(
                request.getPhone()
        );

        student.setGender(
                request.getGender()
        );

        student.setDateOfBirth(
                request.getDateOfBirth()
        );

        student.setAddress(
                request.getAddress()
        );

        student.setEnrollmentNumber(
                request.getEnrollmentNumber()
        );

        student.setDepartment(
                request.getDepartment()
        );

        student.setSemester(
                request.getSemester()
        );

        student.setUniversity(
                university
        );


        // --------------------------------------
        // Save Student
        // --------------------------------------

        Student savedStudent =
                studentRepository.save(
                        student
                );


        return mapToResponse(
                savedStudent
        );
    }


    // ==========================================
    // Get All Students
    // ==========================================

    @Override
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ==========================================
    // Get All Students With Pagination
    // ==========================================

    @Override
    public Page<StudentResponse> getAllStudents(
            Pageable pageable) {

        return studentRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }


    // ==========================================
    // Search Student By Name
    // ==========================================

    @Override
    public List<StudentResponse> searchStudentByName(
            String studentName) {

        return studentRepository
                .findByStudentNameContainingIgnoreCase(
                        studentName
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ==========================================
    // Get Student By ID
    // ==========================================

    @Override
    public StudentResponse getStudentById(
            Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id : "
                                                + id
                                )
                        );


        return mapToResponse(
                student
        );
    }


    // ==========================================
    // Update Student
    // ==========================================

    @Override
    public StudentResponse updateStudent(
            Long id,
            StudentRequest request) {


        // --------------------------------------
        // Find Existing Student
        // --------------------------------------

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id : "
                                                + id
                                )
                        );


        // --------------------------------------
        // Duplicate Email Check
        //
        // Ignore current student's own email.
        // --------------------------------------

        if (studentRepository
                .existsByEmailAndIdNot(
                        request.getEmail(),
                        id
                )) {

            throw new RuntimeException(
                    "Email already exists."
            );
        }


        // --------------------------------------
        // Duplicate Phone Check
        //
        // Ignore current student's own phone.
        // --------------------------------------

        if (studentRepository
                .existsByPhoneAndIdNot(
                        request.getPhone(),
                        id
                )) {

            throw new RuntimeException(
                    "Phone number already exists."
            );
        }


        // --------------------------------------
        // Duplicate Enrollment Check
        //
        // Ignore current student's own
        // enrollment number.
        // --------------------------------------

        if (studentRepository
                .existsByEnrollmentNumberAndIdNot(
                        request.getEnrollmentNumber(),
                        id
                )) {

            throw new RuntimeException(
                    "Enrollment number already exists."
            );
        }


        // --------------------------------------
        // Find University
        // --------------------------------------

        University university =
                universityRepository.findById(
                        request.getUniversityId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "University not found with id : "
                                        + request.getUniversityId()
                        )
                );


        // --------------------------------------
        // Update Student
        // --------------------------------------

        student.setStudentName(
                request.getStudentName()
        );

        student.setEmail(
                request.getEmail()
        );

        student.setPhone(
                request.getPhone()
        );

        student.setGender(
                request.getGender()
        );

        student.setDateOfBirth(
                request.getDateOfBirth()
        );

        student.setAddress(
                request.getAddress()
        );

        student.setEnrollmentNumber(
                request.getEnrollmentNumber()
        );

        student.setDepartment(
                request.getDepartment()
        );

        student.setSemester(
                request.getSemester()
        );

        student.setUniversity(
                university
        );


        // --------------------------------------
        // Save Updated Student
        // --------------------------------------

        Student updatedStudent =
                studentRepository.save(
                        student
                );


        return mapToResponse(
                updatedStudent
        );
    }


    // ==========================================
    // Delete Student
    // ==========================================

    @Override
    public void deleteStudent(
            Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id : "
                                                + id
                                )
                        );


        studentRepository.delete(
                student
        );
    }


    // ==========================================
    // Convert Entity → Response DTO
    // ==========================================

    private StudentResponse mapToResponse(
            Student student) {

        return new StudentResponse(

                student.getId(),

                student.getStudentName(),

                student.getEmail(),

                student.getPhone(),

                student.getGender(),

                student.getDateOfBirth(),

                student.getAddress(),

                student.getEnrollmentNumber(),

                student.getDepartment(),

                student.getSemester(),

                student.getUniversity().getId(),

                student.getUniversity()
                        .getUniversityName()
        );
    }
}