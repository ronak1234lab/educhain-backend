package com.educhain.config;

import com.educhain.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // ==========================================
    // Constructor
    // ==========================================

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ==========================================
    // Security Filter Chain
    // ==========================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // Disable CSRF for REST API
                .csrf(csrf -> csrf.disable())

                // Enable CORS
                .cors(Customizer.withDefaults())

                // JWT authentication is stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================================
                // Authorization Rules
                // ==========================================

                .authorizeHttpRequests(auth -> auth

                        // ----------------------------------
                        // Public Authentication APIs
                        // ----------------------------------

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // ----------------------------------
                        // Public QR Verification
                        // ----------------------------------

                        .requestMatchers(
                                "/api/certificates/public/verify/**"
                        ).permitAll()

                        // ----------------------------------
                        // Public Blockchain Status
                        // ----------------------------------

                        .requestMatchers(
                                "/api/blockchain/status"
                        ).permitAll()

                        // ----------------------------------
                        // Admin APIs
                        // ----------------------------------

                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        // ----------------------------------
                        // University APIs
                        // ----------------------------------

                        .requestMatchers(
                                "/api/universities/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "UNIVERSITY"
                        )

                        // ----------------------------------
                        // Student APIs
                        // ----------------------------------

                        .requestMatchers(
                                "/api/students/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "STUDENT",
                                "UNIVERSITY"
                        )

                        // ----------------------------------
                        // Course APIs
                        // ----------------------------------

                        .requestMatchers(
                                "/api/courses/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "UNIVERSITY"
                        )

                        // ----------------------------------
                        // Certificate APIs
                        // ----------------------------------

                        .requestMatchers(
                                "/api/certificates/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "UNIVERSITY",
                                "STUDENT",
                                "EMPLOYER"
                        )

                        // ----------------------------------
                        // All Other APIs
                        // ----------------------------------

                        .anyRequest().authenticated()
                )

                // ==========================================
                // JWT Filter
                // ==========================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ==========================================
    // CORS Configuration
    // ==========================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // ==========================================
    // Password Encoder
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}