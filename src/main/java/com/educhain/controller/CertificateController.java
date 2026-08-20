package com.educhain.controller;

import com.educhain.dto.request.CertificateRequest;
import com.educhain.dto.response.CertificateResponse;
import com.educhain.entity.Certificate;
import com.educhain.pdf.PdfService;
import com.educhain.qr.QrCodeService;
import com.educhain.repository.CertificateRepository;
import com.educhain.service.CertificateService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    private final PdfService pdfService;

    private final QrCodeService qrCodeService;

    private final CertificateRepository certificateRepository;

    // ==========================================
    // Constructor
    // ==========================================

    public CertificateController(
            CertificateService certificateService,
            PdfService pdfService,
            QrCodeService qrCodeService,
            CertificateRepository certificateRepository) {

        this.certificateService =
                certificateService;

        this.pdfService =
                pdfService;

        this.qrCodeService =
                qrCodeService;

        this.certificateRepository =
                certificateRepository;
    }

    // ==========================================
    // Issue Certificate
    // ==========================================

    @PostMapping
    public CertificateResponse issueCertificate(
            @Valid @RequestBody CertificateRequest request) {

        return certificateService.issueCertificate(
                request
        );
    }

    // ==========================================
    // Get All Certificates
    // ==========================================

    @GetMapping
    public List<CertificateResponse> getAllCertificates() {

        return certificateService.getAllCertificates();
    }

    // ==========================================
    // Pagination
    // ==========================================

    @GetMapping("/page")
    public Page<CertificateResponse> getAllCertificates(
            @PageableDefault(
                    size = 5,
                    sort = "id"
            )
            Pageable pageable) {

        return certificateService.getAllCertificates(
                pageable
        );
    }

    // ==========================================
    // Get Certificate By ID
    // ==========================================

    @GetMapping("/{id}")
    public CertificateResponse getCertificateById(
            @PathVariable Long id) {

        return certificateService.getCertificateById(
                id
        );
    }

    // ==========================================
    // Verify By Certificate Number
    // ==========================================

    @GetMapping("/number/{certificateNumber}")
    public CertificateResponse getCertificateByNumber(
            @PathVariable String certificateNumber) {

        return certificateService.getCertificateByNumber(
                certificateNumber
        );
    }

    // ==========================================
    // Verify By Hash
    // ==========================================

    @GetMapping("/hash/{hash}")
    public CertificateResponse verifyCertificateByHash(
            @PathVariable String hash) {

        return certificateService.verifyCertificateByHash(
                hash
        );
    }

    // ==========================================
    // Public QR Verification
    // ==========================================

    @GetMapping("/public/verify/{hash}")
    public CertificateResponse publicQrVerification(
            @PathVariable String hash) {

        return certificateService.verifyCertificateByHash(
                hash
        );
    }

    // ==========================================
    // Revoke Certificate
    // ==========================================

    @PutMapping("/revoke/{id}")
    public CertificateResponse revokeCertificate(
            @PathVariable Long id) {

        return certificateService.revokeCertificate(
                id
        );
    }

    // ==========================================
    // Download Certificate PDF
    // ==========================================

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCertificate(
            @PathVariable Long id) {

        Certificate certificate =
                certificateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certificate not found"
                                )
                        );

        byte[] pdf =
                pdfService.generateCertificatePdf(
                        certificate
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }

    // ==========================================
    // Generate QR Code
    // ==========================================

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> generateQrCode(
            @PathVariable Long id) {

        Certificate certificate =
                certificateRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certificate not found"
                                )
                        );

        // --------------------------------------
        // QR Verification URL
        // --------------------------------------
        /*
         * PC IPv4 Address:
         * 192.168.0.106
         *
         * The phone must be connected to the
         * same Wi-Fi network as this PC.
         */

        String verificationUrl =
                "http://192.168.0.106:8080/api/certificates/public/verify/"
                        + certificate.getHash();

        // --------------------------------------
        // Generate QR Code
        // --------------------------------------

        byte[] qrCode =
                qrCodeService.generateQRCode(
                        verificationUrl
                );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=qrcode.png"
                )
                .contentType(
                        MediaType.IMAGE_PNG
                )
                .body(qrCode);
    }
}