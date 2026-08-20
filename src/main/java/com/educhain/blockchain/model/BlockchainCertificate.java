package com.educhain.blockchain.model;

public class BlockchainCertificate {

    private String certificateNumber;
    private String hash;

    public BlockchainCertificate() {
    }

    public BlockchainCertificate(String certificateNumber, String hash) {
        this.certificateNumber = certificateNumber;
        this.hash = hash;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}