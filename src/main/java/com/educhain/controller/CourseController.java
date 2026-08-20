package com.educhain.controller;

import com.educhain.dto.request.CourseRequest;
import com.educhain.dto.response.CourseResponse;
import com.educhain.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Create Course
    @PostMapping
    public CourseResponse createCourse(@Valid @RequestBody CourseRequest request) {
        return courseService.saveCourse(request);
    }

    // Get All Courses
    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Get Course By Id
    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    // Update Course
    @PutMapping("/{id}")
    public CourseResponse updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {

        return courseService.updateCourse(id, request);
    }

    // Delete Course
    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {

        courseService.deleteCourse(id);
        return "Course deleted successfully!";
    }

    // Search Course by Name
    @GetMapping("/search")
    public List<CourseResponse> searchCourseByName(
            @RequestParam String name) {

        return courseService.searchCourseByName(name);
    }

    // Search Course by Department
    @GetMapping("/department")
    public List<CourseResponse> getCoursesByDepartment(
            @RequestParam String department) {

        return courseService.getCoursesByDepartment(department);
    }

    // Pagination
    @GetMapping("/page")
    public Page<CourseResponse> getCoursesWithPagination(
            @PageableDefault(size = 5, sort = "courseName") Pageable pageable) {

        return courseService.getAllCourses(pageable);
    }
}