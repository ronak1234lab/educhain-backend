package com.educhain.service.impl;

import com.educhain.dto.request.CourseRequest;
import com.educhain.dto.response.CourseResponse;
import com.educhain.entity.Course;
import com.educhain.entity.University;
import com.educhain.exception.ResourceNotFoundException;
import com.educhain.repository.CourseRepository;
import com.educhain.repository.UniversityRepository;
import com.educhain.service.CourseService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final UniversityRepository universityRepository;


    // ==========================================
    // Constructor
    // ==========================================

    public CourseServiceImpl(
            CourseRepository courseRepository,
            UniversityRepository universityRepository) {

        this.courseRepository =
                courseRepository;

        this.universityRepository =
                universityRepository;
    }


    // ==========================================
    // Create Course
    // ==========================================

    @Override
    public CourseResponse saveCourse(
            CourseRequest request) {


        // --------------------------------------
        // Duplicate Course Code Check
        // --------------------------------------

        if (courseRepository.existsByCourseCode(
                request.getCourseCode())) {

            throw new RuntimeException(
                    "Course Code already exists."
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
        // Create Course
        // --------------------------------------

        Course course =
                new Course();


        course.setCourseName(
                request.getCourseName()
        );

        course.setCourseCode(
                request.getCourseCode()
        );

        course.setDepartment(
                request.getDepartment()
        );

        course.setCredits(
                request.getCredits()
        );

        course.setDuration(
                request.getDuration()
        );

        course.setDescription(
                request.getDescription()
        );

        course.setUniversity(
                university
        );


        // --------------------------------------
        // Save Course
        // --------------------------------------

        Course savedCourse =
                courseRepository.save(
                        course
                );


        return mapToResponse(
                savedCourse
        );
    }


    // ==========================================
    // Get All Courses
    // ==========================================

    @Override
    public List<CourseResponse> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ==========================================
    // Get All Courses With Pagination
    // ==========================================

    @Override
    public Page<CourseResponse> getAllCourses(
            Pageable pageable) {

        return courseRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }


    // ==========================================
    // Search Course By Name
    // ==========================================

    @Override
    public List<CourseResponse> searchCourseByName(
            String courseName) {

        return courseRepository
                .findByCourseNameContainingIgnoreCase(
                        courseName
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ==========================================
    // Search Course By Department
    // ==========================================

    @Override
    public List<CourseResponse> getCoursesByDepartment(
            String department) {

        return courseRepository
                .findByDepartmentIgnoreCase(
                        department
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ==========================================
    // Get Course By ID
    // ==========================================

    @Override
    public CourseResponse getCourseById(
            Long id) {

        Course course =
                courseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id : "
                                                + id
                                )
                        );


        return mapToResponse(
                course
        );
    }


    // ==========================================
    // Update Course
    // ==========================================

    @Override
    public CourseResponse updateCourse(
            Long id,
            CourseRequest request) {


        // --------------------------------------
        // Find Existing Course
        // --------------------------------------

        Course course =
                courseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id : "
                                                + id
                                )
                        );


        // --------------------------------------
        // Duplicate Course Code Check
        //
        // Ignore the current course itself.
        // --------------------------------------

        if (courseRepository
                .existsByCourseCodeAndIdNot(
                        request.getCourseCode(),
                        id
                )) {

            throw new RuntimeException(
                    "Course Code already exists."
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
        // Update Course
        // --------------------------------------

        course.setCourseName(
                request.getCourseName()
        );

        course.setCourseCode(
                request.getCourseCode()
        );

        course.setDepartment(
                request.getDepartment()
        );

        course.setCredits(
                request.getCredits()
        );

        course.setDuration(
                request.getDuration()
        );

        course.setDescription(
                request.getDescription()
        );

        course.setUniversity(
                university
        );


        // --------------------------------------
        // Save Updated Course
        // --------------------------------------

        Course updatedCourse =
                courseRepository.save(
                        course
                );


        return mapToResponse(
                updatedCourse
        );
    }


    // ==========================================
    // Delete Course
    // ==========================================

    @Override
    public void deleteCourse(
            Long id) {

        Course course =
                courseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id : "
                                                + id
                                )
                        );


        courseRepository.delete(
                course
        );
    }


    // ==========================================
    // Convert Entity → Response DTO
    // ==========================================

    private CourseResponse mapToResponse(
            Course course) {

        return new CourseResponse(

                course.getId(),

                course.getCourseName(),

                course.getCourseCode(),

                course.getDepartment(),

                course.getCredits(),

                course.getDuration(),

                course.getDescription(),

                course.getUniversity().getId(),

                course.getUniversity()
                        .getUniversityName()
        );
    }
}