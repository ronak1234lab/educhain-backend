package com.educhain.service.impl;

import com.educhain.dto.request.UniversityRequest;
import com.educhain.dto.response.UniversityResponse;
import com.educhain.entity.University;
import com.educhain.exception.ResourceNotFoundException;
import com.educhain.repository.UniversityRepository;
import com.educhain.service.UniversityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityServiceImpl(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Override
    public UniversityResponse saveUniversity(UniversityRequest request) {

        University university = new University();

        university.setUniversityName(request.getUniversityName());
        university.setEmail(request.getEmail());
        university.setAddress(request.getAddress());
        university.setPhone(request.getPhone());

        University savedUniversity = universityRepository.save(university);

        return mapToResponse(savedUniversity);
    }

    @Override
    public List<UniversityResponse> getAllUniversities() {

        return universityRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UniversityResponse> getAllUniversities(Pageable pageable) {

        Page<University> universityPage = universityRepository.findAll(pageable);

        List<UniversityResponse> responses = universityPage
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(
                responses,
                pageable,
                universityPage.getTotalElements()
        );
    }

    @Override
    public List<UniversityResponse> searchUniversityByName(String universityName) {

        List<University> universities = universityRepository
                .findByUniversityNameContainingIgnoreCase(universityName);

        return universities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UniversityResponse getUniversityById(Long id) {

        University university = universityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("University not found with id: " + id));

        return mapToResponse(university);
    }

    @Override
    public UniversityResponse updateUniversity(Long id, UniversityRequest request) {

        University university = universityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("University not found with id: " + id));

        university.setUniversityName(request.getUniversityName());
        university.setEmail(request.getEmail());
        university.setAddress(request.getAddress());
        university.setPhone(request.getPhone());

        University updatedUniversity = universityRepository.save(university);

        return mapToResponse(updatedUniversity);
    }

    @Override
    public void deleteUniversity(Long id) {

        University university = universityRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("University not found with id: " + id));

        universityRepository.delete(university);
    }

    /**
     * Convert Entity to Response DTO
     */
    private UniversityResponse mapToResponse(University university) {

        return new UniversityResponse(
                university.getId(),
                university.getUniversityName(),
                university.getEmail(),
                university.getAddress(),
                university.getPhone()
        );
    }
}