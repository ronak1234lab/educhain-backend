package com.educhain.controller;

import com.educhain.dto.LoginRequest;
import com.educhain.dto.LoginResponse;
import com.educhain.dto.request.RegisterRequest;
import com.educhain.dto.response.RegisterResponse;
import com.educhain.entity.User;
import com.educhain.repository.UserRepository;
import com.educhain.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:5175"
})
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    // ==========================================
    // Constructor
    // ==========================================

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;
    }


    // ==========================================
    // LOGIN
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        // ======================================
        // Validate Email
        // ======================================

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    new LoginResponse(
                            false,
                            "Email is required",
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Validate Password
        // ======================================

        if (request.getPassword() == null ||
                request.getPassword().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    new LoginResponse(
                            false,
                            "Password is required",
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Find User
        // ======================================

        User user = userRepository
                .findByEmail(
                        request.getEmail().trim()
                )
                .orElse(null);


        // ======================================
        // User Not Found
        // ======================================

        if (user == null) {

            return ResponseEntity.status(401).body(
                    new LoginResponse(
                            false,
                            "Invalid email or password",
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Check Password
        // ======================================

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            return ResponseEntity.status(401).body(
                    new LoginResponse(
                            false,
                            "Invalid email or password",
                            null,
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Generate JWT
        // ======================================

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );


        // ======================================
        // Successful Login
        // ======================================

        return ResponseEntity.ok(
                new LoginResponse(
                        true,
                        "Login successful",
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        token
                )
        );
    }


    // ==========================================
    // REGISTER USER
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {

        // ======================================
        // Validate Name
        // ======================================

        if (request.getName() == null ||
                request.getName().trim().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    new RegisterResponse(
                            false,
                            "Name is required",
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Validate Email
        // ======================================

        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    new RegisterResponse(
                            false,
                            "Email is required",
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Validate Password
        // ======================================

        if (request.getPassword() == null ||
                request.getPassword().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    new RegisterResponse(
                            false,
                            "Password is required",
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Validate Role
        // ======================================

        if (request.getRole() == null ||
                request.getRole().trim().isEmpty()) {

            return ResponseEntity.badRequest().body(
                    new RegisterResponse(
                            false,
                            "Role is required",
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Clean Input
        // ======================================

        String name =
                request.getName().trim();

        String email =
                request.getEmail().trim().toLowerCase();

        String role =
                request.getRole().trim().toUpperCase();


        // ======================================
        // Validate Role
        // ======================================

        Set<String> allowedRoles = Set.of(
                "ADMIN",
                "UNIVERSITY",
                "STUDENT",
                "EMPLOYER"
        );

        if (!allowedRoles.contains(role)) {

            return ResponseEntity.badRequest().body(
                    new RegisterResponse(
                            false,
                            "Invalid role. Allowed roles: ADMIN, UNIVERSITY, STUDENT, EMPLOYER",
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Check Existing Email
        // ======================================

        if (userRepository.existsByEmail(email)) {

            return ResponseEntity.status(409).body(
                    new RegisterResponse(
                            false,
                            "Email already registered",
                            null,
                            null,
                            null,
                            null
                    )
            );
        }


        // ======================================
        // Create User
        // ======================================

        User user = new User();

        user.setName(name);

        user.setEmail(email);


        // ======================================
        // IMPORTANT:
        // Store BCrypt Password
        // ======================================

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        user.setRole(role);


        // ======================================
        // Save User
        // ======================================

        User savedUser =
                userRepository.save(user);


        // ======================================
        // Registration Response
        // ======================================

        return ResponseEntity.ok(
                new RegisterResponse(
                        true,
                        "User registered successfully",
                        savedUser.getId(),
                        savedUser.getName(),
                        savedUser.getEmail(),
                        savedUser.getRole()
                )
        );
    }
}