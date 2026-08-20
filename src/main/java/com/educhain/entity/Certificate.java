package com.educhain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // Certificate belongs to one Student
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    private Student student;

    // ==========================================
    // Certificate belongs to one Course
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private Course course;

    // ==========================================
    // Certificate Number
    // ==========================================

    @Column(
            name = "certificate_number",
            nullable = false,
            unique = true
    )
    private String certificateNumber;

    // ==========================================
    // Issue Date
    // ==========================================

    @Column(
            name = "issue_date",
            nullable = false
    )
    private LocalDate issueDate;

    // ==========================================
    // SHA-256 Certificate Hash
    // ==========================================

    @Column(
            nullable = false,
            length = 64,
            unique = true
    )
    private String hash;

    // ==========================================
    // Certificate Status
    // ==========================================

    @Column(
            nullable = false
    )
    private String status;

    // ==========================================
    // Blockchain Transaction Hash
    // ==========================================

    @Column(
            name = "blockchain_transaction_hash",
            length = 100
    )
    private String blockchainTransactionHash;

    // ==========================================
    // Default Constructor
    // ==========================================

    public Certificate() {
    }

    // ==========================================
    // Parameterized Constructor
    // ==========================================

    public Certificate(
            Long id,
            Student student,
            Course course,
            String certificateNumber,
            LocalDate issueDate,
            String hash,
            String status,
            String blockchainTransactionHash) {

        this.id = id;

        this.student = student;

        this.course = course;

        this.certificateNumber =
                certificateNumber;

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
    // Get Student
    // ==========================================

    public Student getStudent() {

        return student;
    }

    // ==========================================
    // Set Student
    // ==========================================

    public void setStudent(Student student) {

        this.student = student;
    }

    // ==========================================
    // Get Course
    // ==========================================

    public Course getCourse() {

        return course;
    }

    // ==========================================
    // Set Course
    // ==========================================

    public void setCourse(Course course) {

        this.course = course;
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

        this.hash = hash;
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

    public void setStatus(String status) {

        this.status = status;
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