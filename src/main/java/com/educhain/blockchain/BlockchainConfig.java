package com.educhain.blockchain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class BlockchainConfig {

    private static final String RPC_URL =
            "http://127.0.0.1:8545";

    public static final String CONTRACT_ADDRESS =
            "0x5FbDB2315678afecb367f032d93F642f64180aa3";

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(RPC_URL));
    }
}