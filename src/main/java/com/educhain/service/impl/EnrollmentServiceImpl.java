package com.educhain.service.impl;

import com.educhain.dto.request.EnrollmentRequest;
import com.educhain.dto.response.EnrollmentResponse;
import com.educhain.entity.Course;
import com.educhain.entity.Enrollment;
import com.educhain.entity.Student;
import com.educhain.exception.ResourceNotFoundException;
import com.educhain.repository.CourseRepository;
import com.educhain.repository.EnrollmentRepository;
import com.educhain.repository.StudentRepository;
import com.educhain.service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public EnrollmentResponse saveEnrollment(EnrollmentRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : " + request.getStudentId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + request.getCourseId()));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Student is already enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return mapToResponse(savedEnrollment);
    }

    @Override
    public List<EnrollmentResponse> getAllEnrollments() {

        return enrollmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EnrollmentResponse> getAllEnrollments(Pageable pageable) {

        return enrollmentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public EnrollmentResponse getEnrollmentById(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enrollment not found with id : " + id));

        return mapToResponse(enrollment);
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : " + studentId));

        return enrollmentRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : " + courseId));

        return enrollmentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEnrollment(Long id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enrollment not found with id : " + id));

        enrollmentRepository.delete(enrollment);
    }

    /**
     * Convert Entity to Response DTO
     */
    private EnrollmentResponse mapToResponse(Enrollment enrollment) {

        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getStudentName(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getCourseName(),
                enrollment.getCourse().getUniversity().getId(),
                enrollment.getCourse().getUniversity().getUniversityName(),
                enrollment.getEnrollmentDate()
        );
    }
}