package com.educhain.service.impl;

import com.educhain.blockchain.EduChainCredentialClient;
import com.educhain.dto.request.CertificateRequest;
import com.educhain.dto.response.CertificateResponse;
import com.educhain.entity.Certificate;
import com.educhain.entity.Course;
import com.educhain.entity.Student;
import com.educhain.exception.ResourceNotFoundException;
import com.educhain.repository.CertificateRepository;
import com.educhain.repository.CourseRepository;
import com.educhain.repository.StudentRepository;
import com.educhain.service.CertificateService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl
        implements CertificateService {

    private final CertificateRepository certificateRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final EduChainCredentialClient credentialClient;

    // ==========================================
    // Constructor
    // ==========================================

    public CertificateServiceImpl(
            CertificateRepository certificateRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EduChainCredentialClient credentialClient) {

        this.certificateRepository =
                certificateRepository;

        this.studentRepository =
                studentRepository;

        this.courseRepository =
                courseRepository;

        this.credentialClient =
                credentialClient;
    }

    // ==========================================
    // Issue Certificate
    // ==========================================

    @Override
    public CertificateResponse issueCertificate(
            CertificateRequest request) {

        // --------------------------------------
        // Find Student
        // --------------------------------------

        Student student =
                studentRepository.findById(
                        request.getStudentId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id : "
                                        + request.getStudentId()
                        )
                );

        // --------------------------------------
        // Find Course
        // --------------------------------------

        Course course =
                courseRepository.findById(
                        request.getCourseId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id : "
                                        + request.getCourseId()
                        )
                );

        // --------------------------------------
        // Generate Certificate Number
        // --------------------------------------

        String certificateNumber =
                generateCertificateNumber();

        // --------------------------------------
        // Generate SHA-256 Hash
        // --------------------------------------

        String hash =
                generateHash(
                        student.getStudentName(),
                        course.getCourseName(),
                        certificateNumber
                );

        // --------------------------------------
        // Create Certificate
        // --------------------------------------

        Certificate certificate =
                new Certificate();

        certificate.setStudent(
                student
        );

        certificate.setCourse(
                course
        );

        certificate.setCertificateNumber(
                certificateNumber
        );

        certificate.setIssueDate(
                LocalDate.now()
        );

        certificate.setHash(
                hash
        );

        certificate.setStatus(
                "ACTIVE"
        );

        // --------------------------------------
        // Store Credential on Real Blockchain
        // --------------------------------------

        String transactionHash;

        try {

            transactionHash =
                    credentialClient.issueCredential(
                            certificateNumber,
                            hash
                    );

            // ----------------------------------
            // Store Blockchain Transaction Hash
            // ----------------------------------

            certificate.setBlockchainTransactionHash(
                    transactionHash
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "CERTIFICATE STORED ON BLOCKCHAIN"
            );

            System.out.println(
                    "Certificate Number : "
                            + certificateNumber
            );

            System.out.println(
                    "Certificate Hash   : "
                            + hash
            );

            System.out.println(
                    "Transaction Hash   : "
                            + transactionHash
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Blockchain issuance failed: "
                            + e.getMessage(),
                    e
            );
        }

        // --------------------------------------
        // Save Certificate to Database
        // --------------------------------------

        Certificate savedCertificate =
                certificateRepository.save(
                        certificate
                );

        return mapToResponse(
                savedCertificate
        );
    }

    // ==========================================
    // Get All Certificates
    // ==========================================

    @Override
    public List<CertificateResponse>
    getAllCertificates() {

        return certificateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Get All Certificates With Pagination
    // ==========================================

    @Override
    public Page<CertificateResponse>
    getAllCertificates(
            Pageable pageable) {

        return certificateRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }

    // ==========================================
    // Get Certificate By ID
    // ==========================================

    @Override
    public CertificateResponse getCertificateById(
            Long id) {

        Certificate certificate =
                certificateRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Certificate not found with id : "
                                                + id
                                )
                        );

        return mapToResponse(
                certificate
        );
    }

    // ==========================================
    // Get Certificate By Number
    // ==========================================

    @Override
    public CertificateResponse getCertificateByNumber(
            String certificateNumber) {

        // --------------------------------------
        // Validate Certificate Number
        // --------------------------------------

        if (certificateNumber == null ||
                certificateNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Certificate number is required."
            );
        }

        String normalizedNumber =
                certificateNumber.trim();

        // --------------------------------------
        // Find Certificate
        // --------------------------------------

        Certificate certificate =
                certificateRepository
                        .findByCertificateNumber(
                                normalizedNumber
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Certificate not found : "
                                                + normalizedNumber
                                )
                        );

        // --------------------------------------
        // Check Database Status
        // --------------------------------------

        if ("REVOKED".equalsIgnoreCase(
                certificate.getStatus())) {

            throw new IllegalStateException(
                    "Certificate has been revoked."
            );
        }

        // --------------------------------------
        // Verify on Real Blockchain
        // --------------------------------------

        try {

            boolean verified =
                    credentialClient.verifyCredential(
                            certificate.getCertificateNumber(),
                            certificate.getHash()
                    );

            if (!verified) {

                throw new IllegalStateException(
                        "Certificate verification failed on blockchain."
                );
            }

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to verify certificate on blockchain: "
                            + e.getMessage(),
                    e
            );
        }

        return mapToResponse(
                certificate
        );
    }

    // ==========================================
    // Verify Certificate By Hash
    // ==========================================

    @Override
    public CertificateResponse verifyCertificateByHash(
            String hash) {

        // --------------------------------------
        // Validate Hash
        // --------------------------------------

        if (hash == null ||
                hash.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Certificate hash is required."
            );
        }

        String normalizedHash =
                hash.trim().toLowerCase();

        // --------------------------------------
        // Find Certificate in Database
        // --------------------------------------

        Certificate certificate =
                certificateRepository
                        .findByHash(normalizedHash)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Certificate not found for given hash."
                                )
                        );

        // --------------------------------------
        // Check Database Status
        // --------------------------------------

        if ("REVOKED".equalsIgnoreCase(
                certificate.getStatus())) {

            throw new IllegalStateException(
                    "Certificate has been revoked."
            );
        }

        // --------------------------------------
        // Verify on Real Blockchain
        // --------------------------------------

        try {

            boolean verified =
                    credentialClient.verifyCredential(
                            certificate.getCertificateNumber(),
                            normalizedHash
                    );

            if (!verified) {

                throw new IllegalStateException(
                        "Certificate verification failed on blockchain."
                );
            }

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to verify certificate on blockchain: "
                            + e.getMessage(),
                    e
            );
        }

        // --------------------------------------
        // Return Verified Certificate
        // --------------------------------------

        return mapToResponse(
                certificate
        );
    }

    // ==========================================
    // Revoke Certificate
    // ==========================================

    @Override
    public CertificateResponse revokeCertificate(
            Long id) {

        // --------------------------------------
        // Find Certificate
        // --------------------------------------

        Certificate certificate =
                certificateRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Certificate not found with id : "
                                                + id
                                )
                        );

        // --------------------------------------
        // Already Revoked
        // --------------------------------------

        if ("REVOKED".equalsIgnoreCase(
                certificate.getStatus())) {

            return mapToResponse(
                    certificate
            );
        }

        // --------------------------------------
        // Revoke on Real Blockchain First
        // --------------------------------------

        try {

            String transactionHash =
                    credentialClient.revokeCredential(
                            certificate.getCertificateNumber()
                    );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "CERTIFICATE REVOKED ON BLOCKCHAIN"
            );

            System.out.println(
                    "Certificate Number : "
                            + certificate.getCertificateNumber()
            );

            System.out.println(
                    "Blockchain Transaction Hash : "
                            + transactionHash
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Blockchain revocation failed: "
                            + e.getMessage(),
                    e
            );
        }

        // --------------------------------------
        // Update Database Status
        // --------------------------------------

        certificate.setStatus(
                "REVOKED"
        );

        Certificate updatedCertificate =
                certificateRepository.save(
                        certificate
                );

        return mapToResponse(
                updatedCertificate
        );
    }

    // ==========================================
    // Generate Certificate Number
    // ==========================================

    private String generateCertificateNumber() {

        String certificateNumber;

        do {

            certificateNumber =
                    "CERT-"
                            + LocalDate.now().getYear()
                            + "-"
                            + UUID.randomUUID()
                            .toString()
                            .substring(0, 8)
                            .toUpperCase();

        } while (
                certificateRepository
                        .existsByCertificateNumber(
                                certificateNumber
                        )
        );

        return certificateNumber;
    }

    // ==========================================
    // Generate SHA-256 Hash
    // ==========================================

    private String generateHash(
            String studentName,
            String courseName,
            String certificateNumber) {

        try {

            String data =
                    studentName
                            + courseName
                            + certificateNumber
                            + LocalDate.now();

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hashBytes =
                    digest.digest(
                            data.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder hexString =
                    new StringBuilder();

            for (byte b : hashBytes) {

                String hex =
                        Integer.toHexString(
                                0xff & b
                        );

                if (hex.length() == 1) {

                    hexString.append('0');
                }

                hexString.append(
                        hex
                );
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(
                    "SHA-256 Algorithm not found.",
                    e
            );
        }
    }

    // ==========================================
    // Convert Entity → Response DTO
    // ==========================================

    private CertificateResponse mapToResponse(
            Certificate certificate) {

        return new CertificateResponse(

                certificate.getId(),

                certificate.getCertificateNumber(),

                certificate.getStudent().getId(),

                certificate.getStudent()
                        .getStudentName(),

                certificate.getCourse().getId(),

                certificate.getCourse()
                        .getCourseName(),

                certificate.getCourse()
                        .getUniversity()
                        .getId(),

                certificate.getCourse()
                        .getUniversity()
                        .getUniversityName(),

                certificate.getIssueDate(),

                certificate.getHash(),

                certificate.getStatus(),

                certificate.getBlockchainTransactionHash()
        );
    }
}