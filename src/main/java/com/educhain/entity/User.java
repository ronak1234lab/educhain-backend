package com.educhain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    // ==========================================
    // Primary Key
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ==========================================
    // User Email
    // ==========================================

    @Column(nullable = false, unique = true)
    private String email;


    // ==========================================
    // Password
    // ==========================================

    @Column(nullable = false)
    private String password;


    // ==========================================
    // User Name
    // ==========================================

    @Column(nullable = false)
    private String name;


    // ==========================================
    // User Role
    // ==========================================

    @Column(nullable = false)
    private String role;


    // ==========================================
    // Account Created Date
    // ==========================================

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    // ==========================================
    // Default Constructor
    // ==========================================

    public User() {
    }


    // ==========================================
    // Parameterized Constructor
    // ==========================================

    public User(
            String email,
            String password,
            String name,
            String role
    ) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }


    // ==========================================
    // Automatically Set Created Date
    // ==========================================

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


    // ==========================================
    // Getters and Setters
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}