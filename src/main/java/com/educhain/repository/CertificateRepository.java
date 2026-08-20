package com.educhain.repository;

import com.educhain.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    // Check if certificate number already exists
    boolean existsByCertificateNumber(String certificateNumber);

    // Find certificate using certificate number
    Optional<Certificate> findByCertificateNumber(String certificateNumber);

    // Find certificate using blockchain hash
    Optional<Certificate> findByHash(String hash);
}