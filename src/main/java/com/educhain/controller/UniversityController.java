package com.educhain.controller;

import com.educhain.dto.request.UniversityRequest;
import com.educhain.dto.response.UniversityResponse;
import com.educhain.service.UniversityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    // Create University
    @PostMapping
    public UniversityResponse createUniversity(@Valid @RequestBody UniversityRequest request) {
        return universityService.saveUniversity(request);
    }

    // Get All Universities
    @GetMapping
    public List<UniversityResponse> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    // Pagination + Sorting
    @GetMapping("/page")
    public Page<UniversityResponse> getAllUniversitiesWithPagination(
            @PageableDefault(size = 5, sort = "universityName") Pageable pageable) {

        return universityService.getAllUniversities(pageable);
    }

    // Search University
    @GetMapping("/search")
    public List<UniversityResponse> searchUniversityByName(
            @RequestParam String name) {

        return universityService.searchUniversityByName(name);
    }

    // Get University By Id
    @GetMapping("/{id}")
    public UniversityResponse getUniversityById(@PathVariable Long id) {
        return universityService.getUniversityById(id);
    }

    // Update University
    @PutMapping("/{id}")
    public UniversityResponse updateUniversity(
            @PathVariable Long id,
            @Valid @RequestBody UniversityRequest request) {

        return universityService.updateUniversity(id, request);
    }

    // Delete University
    @DeleteMapping("/{id}")
    public String deleteUniversity(@PathVariable Long id) {

        universityService.deleteUniversity(id);
        return "University deleted successfully!";
    }
}