package com.educhain.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {


    private final JwtService jwtService;


    // ==========================================
    // Constructor
    // ==========================================

    public JwtAuthenticationFilter(
            JwtService jwtService
    ) {
        this.jwtService = jwtService;
    }


    // ==========================================
    // JWT Filter
    // ==========================================

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        // ==========================================
        // Get Authorization Header
        // ==========================================

        String authorizationHeader =
                request.getHeader("Authorization");


        // ==========================================
        // Check Bearer Token
        // ==========================================

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }


        // ==========================================
        // Extract JWT
        // ==========================================

        String token =
                authorizationHeader.substring(7).trim();


        // ==========================================
        // Ignore Empty Token
        // ==========================================

        if (token.isEmpty()) {

            filterChain.doFilter(request, response);

            return;
        }


        try {

            // ==========================================
            // Extract Email
            // ==========================================

            String email =
                    jwtService.extractEmail(token);


            // ==========================================
            // Check Email
            // ==========================================

            if (email == null ||
                    email.trim().isEmpty()) {

                filterChain.doFilter(request, response);

                return;
            }


            // ==========================================
            // Check Existing Authentication
            // ==========================================

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {


                // ======================================
                // Validate Token
                // ======================================

                if (jwtService.isTokenValid(
                        token,
                        email
                )) {


                    // ==================================
                    // Extract Role
                    // ==================================

                    String role =
                            jwtService.extractRole(token);


                    // ==================================
                    // Validate Role
                    // ==================================

                    if (role == null ||
                            role.trim().isEmpty()) {

                        filterChain.doFilter(
                                request,
                                response
                        );

                        return;
                    }


                    // ==================================
                    // Normalize Role
                    // ==================================

                    role = role
                            .trim()
                            .toUpperCase(Locale.ROOT);


                    // ==================================
                    // Create Authority
                    // ==================================

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(
                                    "ROLE_" + role
                            );


                    // ==================================
                    // Create Authentication
                    // ==================================

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(authority)
                            );


                    // ==================================
                    // Set Security Context
                    // ==================================

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );
                }
            }


        } catch (Exception e) {

            // ==========================================
            // Invalid / Expired JWT
            // ==========================================

            SecurityContextHolder
                    .clearContext();
        }


        // ==========================================
        // Continue Request
        // ==========================================

        filterChain.doFilter(
                request,
                response
        );
    }
}