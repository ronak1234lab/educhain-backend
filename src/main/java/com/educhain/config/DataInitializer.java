package com.educhain.config;

import com.educhain.entity.User;
import com.educhain.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            String adminEmail = "thulronak@gmail.com";

            if (!userRepository.existsByEmail(adminEmail)) {

                String adminPassword =
                        System.getenv("EDUCHAIN_ADMIN_PASSWORD");

                if (adminPassword == null ||
                        adminPassword.isBlank()) {

                    throw new IllegalStateException(
                            "EDUCHAIN_ADMIN_PASSWORD environment variable is not set."
                    );
                }

                User admin = new User(
                        adminEmail,
                        passwordEncoder.encode(adminPassword),
                        "Ronak Thul",
                        "ADMIN"
                );

                userRepository.save(admin);

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "EduChain Admin account created successfully."
                );

                System.out.println(
                        "Email: " + adminEmail
                );

                System.out.println(
                        "Role: ADMIN"
                );

                System.out.println(
                        "=========================================="
                );

            } else {

                System.out.println(
                        "EduChain Admin account already exists."
                );
            }
        };
    }
}