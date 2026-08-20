package com.educhain.blockchain;

import com.educhain.blockchain.model.BlockchainCertificate;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
public class BlockchainService {

    private final Web3j web3j;

    /*
     * Temporary compatibility storage.
     *
     * Your existing BlockchainController and
     * CertificateServiceImpl currently use:
     *
     * getCertificate()
     * verifyCertificate()
     *
     * We keep these methods working while we
     * progressively move certificate operations
     * to the real smart contract.
     */
    private final Map<String, BlockchainCertificate> blockchainStorage =
            new HashMap<>();

    private static final String CONTRACT_ADDRESS =
            "0x5FbDB2315678afecb367f032d93F642f64180aa3";

    public BlockchainService(Web3j web3j) {
        this.web3j = web3j;
    }

    // ==========================================
    // Check Blockchain Connection
    // ==========================================

    public String getClientVersion() {

        try {

            return web3j
                    .web3ClientVersion()
                    .send()
                    .getWeb3ClientVersion();

        } catch (Exception e) {

            return "Connection Failed : " + e.getMessage();
        }
    }

    // ==========================================
    // Get Chain ID
    // ==========================================

    public BigInteger getChainId() {

        try {

            return web3j
                    .ethChainId()
                    .send()
                    .getChainId();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to get blockchain chain ID",
                    e
            );
        }
    }

    // ==========================================
    // Get Contract Address
    // ==========================================

    public String getContractAddress() {

        return CONTRACT_ADDRESS;
    }

    // ==========================================
    // Store Certificate
    // ==========================================

    public String storeCertificate(
            String certificateNumber,
            String hash
    ) {

        System.out.println(
                "=================================="
        );

        System.out.println(
                "STORING CERTIFICATE"
        );

        System.out.println(
                "Certificate Number : "
                        + certificateNumber
        );

        System.out.println(
                "Hash : "
                        + hash
        );

        BlockchainCertificate certificate =
                new BlockchainCertificate(
                        certificateNumber,
                        hash
                );

        blockchainStorage.put(
                certificateNumber,
                certificate
        );

        System.out.println(
                "Current Storage Size : "
                        + blockchainStorage.size()
        );

        System.out.println(
                "=================================="
        );

        return "Certificate stored successfully.";
    }

    // ==========================================
    // Get Certificate
    // ==========================================

    public BlockchainCertificate getCertificate(
            String certificateNumber
    ) {

        return blockchainStorage.get(
                certificateNumber
        );
    }

    // ==========================================
    // Verify Certificate
    // ==========================================

    public boolean verifyCertificate(
            String hash
    ) {

        if (hash == null ||
                hash.trim().isEmpty()) {

            return false;
        }

        System.out.println(
                "Searching for hash: "
                        + hash
        );

        for (
                BlockchainCertificate certificate
                : blockchainStorage.values()
        ) {

            if (
                    certificate.getHash() != null
                            &&
                            certificate.getHash()
                                    .trim()
                                    .equalsIgnoreCase(
                                            hash.trim()
                                    )
            ) {

                System.out.println(
                        "MATCH FOUND"
                );

                return true;
            }
        }

        System.out.println(
                "NO MATCH FOUND"
        );

        return false;
    }
}