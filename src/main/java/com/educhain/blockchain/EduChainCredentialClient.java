package com.educhain.blockchain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import org.web3j.crypto.Credentials;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;

import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class EduChainCredentialClient {

    private final Web3j web3j;

    // ==========================================
    // Blockchain Configuration
    // ==========================================

    private final String privateKey;

    private final String contractAddress;

    private final BigInteger gasLimit;

    private final BigInteger gasPrice;

    // ==========================================
    // Constructor
    // ==========================================

    public EduChainCredentialClient(

            Web3j web3j,

            @Value("${blockchain.private-key}")
            String privateKey,

            @Value("${blockchain.contract-address}")
            String contractAddress,

            @Value("${blockchain.gas-limit}")
            BigInteger gasLimit,

            @Value("${blockchain.gas-price}")
            BigInteger gasPrice) {

        this.web3j =
                web3j;

        this.privateKey =
                privateKey;

        this.contractAddress =
                contractAddress;

        this.gasLimit =
                gasLimit;

        this.gasPrice =
                gasPrice;
    }

    // ==========================================
    // Transaction Manager
    // ==========================================

    private TransactionManager getTransactionManager()
            throws Exception {

        Credentials credentials =
                Credentials.create(
                        privateKey
                );

        BigInteger chainId =
                web3j.ethChainId()
                        .send()
                        .getChainId();

        return new RawTransactionManager(
                web3j,
                credentials,
                chainId.longValue()
        );
    }

    // ==========================================
    // Gas Provider
    // ==========================================

    private StaticGasProvider getGasProvider() {

        return new StaticGasProvider(
                gasPrice,
                gasLimit
        );
    }

    // ==========================================
    // Issue Credential
    // ==========================================

    public String issueCredential(
            String credentialId,
            String certificateHash
    ) throws Exception {

        // --------------------------------------
        // Validate Credential ID
        // --------------------------------------

        if (credentialId == null ||
                credentialId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Credential ID is required"
            );
        }

        // --------------------------------------
        // Validate Certificate Hash
        // --------------------------------------

        if (certificateHash == null ||
                certificateHash.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Certificate hash is required"
            );
        }

        String normalizedHash =
                certificateHash
                        .trim()
                        .toLowerCase();

        // --------------------------------------
        // Convert Hash to Bytes
        // --------------------------------------

        byte[] hashBytes;

        try {

            hashBytes =
                    Numeric.hexStringToByteArray(
                            normalizedHash
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid certificate hash. "
                            + "Expected hexadecimal hash.",
                    e
            );
        }

        // --------------------------------------
        // Validate 32 Bytes
        // --------------------------------------

        if (hashBytes.length != 32) {

            throw new IllegalArgumentException(
                    "Certificate hash must be exactly "
                            + "32 bytes (64 hexadecimal characters)"
            );
        }

        Bytes32 hash =
                new Bytes32(
                        hashBytes
                );

        // --------------------------------------
        // Build Smart Contract Function
        // --------------------------------------

        Function function =
                new Function(
                        "issueCredential",

                        Arrays.asList(
                                new Utf8String(
                                        credentialId.trim()
                                ),
                                hash
                        ),

                        Collections.emptyList()
                );

        // --------------------------------------
        // Encode Function
        // --------------------------------------

        String encodedFunction =
                FunctionEncoder.encode(
                        function
                );

        // --------------------------------------
        // Get Transaction Manager
        // --------------------------------------

        TransactionManager transactionManager =
                getTransactionManager();

        // --------------------------------------
        // Send Transaction
        // --------------------------------------

        EthSendTransaction transactionResponse =
                transactionManager.sendTransaction(
                        gasPrice,
                        gasLimit,
                        contractAddress,
                        encodedFunction,
                        BigInteger.ZERO
                );

        // --------------------------------------
        // Check Transaction Error
        // --------------------------------------

        if (transactionResponse.hasError()) {

            throw new RuntimeException(
                    "Blockchain transaction failed: "
                            + transactionResponse
                            .getError()
                            .getMessage()
            );
        }

        // --------------------------------------
        // Return Transaction Hash
        // --------------------------------------

        return transactionResponse
                .getTransactionHash();
    }

    // ==========================================
    // Verify Credential
    // ==========================================

    public boolean verifyCredential(
            String credentialId,
            String certificateHash
    ) throws Exception {

        // --------------------------------------
        // Validate Hash
        // --------------------------------------

        if (certificateHash == null ||
                certificateHash.trim().isEmpty()) {

            return false;
        }

        String normalizedHash =
                certificateHash
                        .trim()
                        .toLowerCase();

        // --------------------------------------
        // Convert Hash
        // --------------------------------------

        byte[] hashBytes;

        try {

            hashBytes =
                    Numeric.hexStringToByteArray(
                            normalizedHash
                    );

        } catch (Exception e) {

            return false;
        }

        if (hashBytes.length != 32) {

            return false;
        }

        Bytes32 hash =
                new Bytes32(
                        hashBytes
                );

        // --------------------------------------
        // Build Verify Function
        // --------------------------------------

        Function function =
                new Function(
                        "verifyCredential",

                        Arrays.asList(
                                new Utf8String(
                                        credentialId
                                ),
                                hash
                        ),

                        Collections.singletonList(
                                new TypeReference<Bool>() {
                                }
                        )
                );

        // --------------------------------------
        // Encode Function
        // --------------------------------------

        String encoded =
                FunctionEncoder.encode(
                        function
                );

        // --------------------------------------
        // Blockchain Read
        // --------------------------------------

        EthCall response =
                web3j.ethCall(
                        Transaction.createEthCallTransaction(
                                null,
                                contractAddress,
                                encoded
                        ),
                        DefaultBlockParameterName.LATEST
                ).send();

        if (response.hasError()) {

            return false;
        }

        // --------------------------------------
        // Decode Response
        // --------------------------------------

        List<Type> results =
                FunctionReturnDecoder.decode(
                        response.getValue(),
                        function.getOutputParameters()
                );

        if (results.isEmpty()) {

            return false;
        }

        return (Boolean)
                results
                        .get(0)
                        .getValue();
    }

    // ==========================================
    // Get Credential
    // ==========================================

    public String getCredential(
            String credentialId
    ) throws Exception {

        // --------------------------------------
        // Validate Credential ID
        // --------------------------------------

        if (credentialId == null ||
                credentialId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Credential ID is required"
            );
        }

        // --------------------------------------
        // Build Function
        // --------------------------------------

        Function function =
                new Function(
                        "getCredential",

                        Collections.singletonList(
                                new Utf8String(
                                        credentialId.trim()
                                )
                        ),

                        Arrays.asList(
                                new TypeReference<Utf8String>() {
                                },

                                new TypeReference<Bytes32>() {
                                },

                                new TypeReference<Address>() {
                                },

                                new TypeReference<Uint256>() {
                                },

                                new TypeReference<Bool>() {
                                },

                                new TypeReference<Bool>() {
                                }
                        )
                );

        // --------------------------------------
        // Encode Function
        // --------------------------------------

        String encoded =
                FunctionEncoder.encode(
                        function
                );

        // --------------------------------------
        // Execute Read Call
        // --------------------------------------

        EthCall response =
                web3j.ethCall(
                        Transaction.createEthCallTransaction(
                                null,
                                contractAddress,
                                encoded
                        ),
                        DefaultBlockParameterName.LATEST
                ).send();

        // --------------------------------------
        // Check Error
        // --------------------------------------

        if (response.hasError()) {

            throw new RuntimeException(
                    "Blockchain read failed: "
                            + response
                            .getError()
                            .getMessage()
            );
        }

        // --------------------------------------
        // Decode Response
        // --------------------------------------

        List<Type> results =
                FunctionReturnDecoder.decode(
                        response.getValue(),
                        function.getOutputParameters()
                );

        if (results.size() != 6) {

            return "Credential not found";
        }

        String storedCredentialId =
                (String)
                        results
                                .get(0)
                                .getValue();

        Object rawHash =
                results
                        .get(1)
                        .getValue();

        String storedHash;

        if (rawHash instanceof byte[]) {

            storedHash =
                    Numeric.toHexString(
                            (byte[]) rawHash
                    );

        } else if (rawHash instanceof Bytes32) {

            storedHash =
                    Numeric.toHexString(
                            ((Bytes32) rawHash)
                                    .getValue()
                    );

        } else {

            storedHash =
                    String.valueOf(
                            rawHash
                    );
        }

        String issuer =
                String.valueOf(
                        results
                                .get(2)
                                .getValue()
                );

        BigInteger issueDate =
                (BigInteger)
                        results
                                .get(3)
                                .getValue();

        Boolean revoked =
                (Boolean)
                        results
                                .get(4)
                                .getValue();

        Boolean exists =
                (Boolean)
                        results
                                .get(5)
                                .getValue();

        return "Credential ID: "
                + storedCredentialId
                + "\nCertificate Hash: "
                + storedHash
                + "\nIssuer: "
                + issuer
                + "\nIssue Date: "
                + issueDate
                + "\nRevoked: "
                + revoked
                + "\nExists: "
                + exists;
    }

    // ==========================================
    // Revoke Credential
    // ==========================================

    public String revokeCredential(
            String credentialId
    ) throws Exception {

        // --------------------------------------
        // Validate Credential ID
        // --------------------------------------

        if (credentialId == null ||
                credentialId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Credential ID is required"
            );
        }

        // --------------------------------------
        // Build Function
        // --------------------------------------

        Function function =
                new Function(
                        "revokeCredential",

                        Collections.singletonList(
                                new Utf8String(
                                        credentialId.trim()
                                )
                        ),

                        Collections.emptyList()
                );

        // --------------------------------------
        // Encode Function
        // --------------------------------------

        String encodedFunction =
                FunctionEncoder.encode(
                        function
                );

        // --------------------------------------
        // Transaction Manager
        // --------------------------------------

        TransactionManager transactionManager =
                getTransactionManager();

        // --------------------------------------
        // Send Transaction
        // --------------------------------------

        EthSendTransaction transactionResponse =
                transactionManager.sendTransaction(
                        gasPrice,
                        gasLimit,
                        contractAddress,
                        encodedFunction,
                        BigInteger.ZERO
                );

        // --------------------------------------
        // Check Transaction Error
        // --------------------------------------

        if (transactionResponse.hasError()) {

            throw new RuntimeException(
                    "Blockchain revoke failed: "
                            + transactionResponse
                            .getError()
                            .getMessage()
            );
        }

        // --------------------------------------
        // Return Transaction Hash
        // --------------------------------------

        return transactionResponse
                .getTransactionHash();
    }
}