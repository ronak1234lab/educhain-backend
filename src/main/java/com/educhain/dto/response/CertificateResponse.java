package com.educhain.dto.response;

import java.time.LocalDate;

public class CertificateResponse {

    private Long id;

    private String certificateNumber;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private Long universityId;
    private String universityName;

    private LocalDate issueDate;

    private String hash;

    private String status;

    // ==========================================
    // Blockchain Transaction Hash
    // ==========================================

    private String blockchainTransactionHash;

    // ==========================================
    // Default Constructor
    // ==========================================

    public CertificateResponse() {
    }

    // ==========================================
    // Parameterized Constructor
    // ==========================================

    public CertificateResponse(
            Long id,
            String certificateNumber,
            Long studentId,
            String studentName,
            Long courseId,
            String courseName,
            Long universityId,
            String universityName,
            LocalDate issueDate,
            String hash,
            String status,
            String blockchainTransactionHash) {

        this.id = id;

        this.certificateNumber =
                certificateNumber;

        this.studentId =
                studentId;

        this.studentName =
                studentName;

        this.courseId =
                courseId;

        this.courseName =
                courseName;

        this.universityId =
                universityId;

        this.universityName =
                universityName;

        this.issueDate =
                issueDate;

        this.hash =
                hash;

        this.status =
                status;

        this.blockchainTransactionHash =
                blockchainTransactionHash;
    }

    // ==========================================
    // Get ID
    // ==========================================

    public Long getId() {

        return id;
    }

    // ==========================================
    // Set ID
    // ==========================================

    public void setId(Long id) {

        this.id = id;
    }

    // ==========================================
    // Get Certificate Number
    // ==========================================

    public String getCertificateNumber() {

        return certificateNumber;
    }

    // ==========================================
    // Set Certificate Number
    // ==========================================

    public void setCertificateNumber(
            String certificateNumber) {

        this.certificateNumber =
                certificateNumber;
    }

    // ==========================================
    // Get Student ID
    // ==========================================

    public Long getStudentId() {

        return studentId;
    }

    // ==========================================
    // Set Student ID
    // ==========================================

    public void setStudentId(
            Long studentId) {

        this.studentId =
                studentId;
    }

    // ==========================================
    // Get Student Name
    // ==========================================

    public String getStudentName() {

        return studentName;
    }

    // ==========================================
    // Set Student Name
    // ==========================================

    public void setStudentName(
            String studentName) {

        this.studentName =
                studentName;
    }

    // ==========================================
    // Get Course ID
    // ==========================================

    public Long getCourseId() {

        return courseId;
    }

    // ==========================================
    // Set Course ID
    // ==========================================

    public void setCourseId(
            Long courseId) {

        this.courseId =
                courseId;
    }

    // ==========================================
    // Get Course Name
    // ==========================================

    public String getCourseName() {

        return courseName;
    }

    // ==========================================
    // Set Course Name
    // ==========================================

    public void setCourseName(
            String courseName) {

        this.courseName =
                courseName;
    }

    // ==========================================
    // Get University ID
    // ==========================================

    public Long getUniversityId() {

        return universityId;
    }

    // ==========================================
    // Set University ID
    // ==========================================

    public void setUniversityId(
            Long universityId) {

        this.universityId =
                universityId;
    }

    // ==========================================
    // Get University Name
    // ==========================================

    public String getUniversityName() {

        return universityName;
    }

    // ==========================================
    // Set University Name
    // ==========================================

    public void setUniversityName(
            String universityName) {

        this.universityName =
                universityName;
    }

    // ==========================================
    // Get Issue Date
    // ==========================================

    public LocalDate getIssueDate() {

        return issueDate;
    }

    // ==========================================
    // Set Issue Date
    // ==========================================

    public void setIssueDate(
            LocalDate issueDate) {

        this.issueDate =
                issueDate;
    }

    // ==========================================
    // Get Hash
    // ==========================================

    public String getHash() {

        return hash;
    }

    // ==========================================
    // Set Hash
    // ==========================================

    public void setHash(String hash) {

        this.hash =
                hash;
    }

    // ==========================================
    // Get Status
    // ==========================================

    public String getStatus() {

        return status;
    }

    // ==========================================
    // Set Status
    // ==========================================

    public void setStatus(
            String status) {

        this.status =
                status;
    }

    // ==========================================
    // Get Blockchain Transaction Hash
    // ==========================================

    public String getBlockchainTransactionHash() {

        return blockchainTransactionHash;
    }

    // ==========================================
    // Set Blockchain Transaction Hash
    // ==========================================

    public void setBlockchainTransactionHash(
            String blockchainTransactionHash) {

        this.blockchainTransactionHash =
                blockchainTransactionHash;
    }
}