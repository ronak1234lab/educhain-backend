package com.educhain.service;



import com.educhain.dto.request.UniversityRequest;
import com.educhain.dto.response.UniversityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UniversityService {

    // Create University
    UniversityResponse saveUniversity(UniversityRequest request);

    // Get All Universities
    List<UniversityResponse> getAllUniversities();
    Page<UniversityResponse> getAllUniversities(Pageable pageable);

    // Search University by Name
    List<UniversityResponse> searchUniversityByName(String universityName);

    // Get University by ID
    UniversityResponse getUniversityById(Long id);

    // Update University
    UniversityResponse updateUniversity(Long id, UniversityRequest request);

    // Delete University
    void deleteUniversity(Long id);
}