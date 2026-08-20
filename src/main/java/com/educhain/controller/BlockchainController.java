package com.educhain.controller;

import com.educhain.blockchain.BlockchainService;
import com.educhain.blockchain.model.BlockchainCertificate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockchainController {

    private final BlockchainService blockchainService;

    // ==========================================
    // Constructor
    // ==========================================

    public BlockchainController(
            BlockchainService blockchainService
    ) {
        this.blockchainService = blockchainService;
    }

    // ==========================================
    // Check Blockchain Connection
    // ==========================================

    @GetMapping("/api/blockchain/status")
    public String status() {

        return blockchainService.getClientVersion();
    }

    // ==========================================
    // Get Certificate
    // ==========================================

    @GetMapping(
            "/api/blockchain/certificate/{certificateNumber}"
    )
    public BlockchainCertificate getCertificate(
            @PathVariable String certificateNumber
    ) {

        return blockchainService.getCertificate(
                certificateNumber
        );
    }

    // ==========================================
    // Verify Certificate
    // ==========================================

    @GetMapping(
            "/api/blockchain/verify/{hash}"
    )
    public String verifyCertificate(
            @PathVariable String hash
    ) {

        boolean exists =
                blockchainService.verifyCertificate(
                        hash
                );

        if (exists) {

            return "Certificate Verified on Blockchain";
        }

        return "Certificate Not Found on Blockchain";
    }
}