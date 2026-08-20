package com.educhain.service;

import com.educhain.dto.request.CertificateRequest;
import com.educhain.dto.response.CertificateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificateService {

    // Issue Certificate
    CertificateResponse issueCertificate(CertificateRequest request);

    // Get All Certificates
    List<CertificateResponse> getAllCertificates();

    // Pagination
    Page<CertificateResponse> getAllCertificates(Pageable pageable);

    // Get Certificate By Id
    CertificateResponse getCertificateById(Long id);

    // Verify By Certificate Number
    CertificateResponse getCertificateByNumber(String certificateNumber);

    // Verify By Hash
    CertificateResponse verifyCertificateByHash(String hash);

    // Revoke Certificate
    CertificateResponse revokeCertificate(Long id);
}