package com.college.placement.service;

import com.college.placement.dto.*;
import com.college.placement.model.*;
import com.college.placement.repository.*;
import com.college.placement.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;
    
    /**
     * Authenticate user and generate token
     */
    public AuthResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().name());
        
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000) // Convert to seconds
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .profile(buildUserProfile(user))
                .build();
    }
    
    /**
     * Register new student
     */
    @Transactional
    public AuthResponse registerStudent(RegisterRequest request) {
        // Check if username exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create or get student record
        Student student = null;
        if (request.getStudentId() != null) {
            student = studentRepository.findById(request.getStudentId())
                    .orElseGet(() -> {
                        Student newStudent = new Student();
                        newStudent.setStudentId(request.getStudentId());
                        newStudent.setName(request.getName());
                        newStudent.setDepartment(request.getDepartment());
                        newStudent.setBatchYear(request.getBatchYear());
                        newStudent.setPlacementStatus(Student.PlacementStatus.NOT_PLACED);
                        return studentRepository.save(newStudent);
                    });
        }
        
        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(User.UserRole.STUDENT)
                .isActive(true)
                .student(student)
                .build();
        
        user = userRepository.save(user);
        
        // Generate token
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().name());
        
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .profile(buildUserProfile(user))
                .build();
    }
    
    /**
     * Get user profile
     */
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return buildUserProfile(user);
    }
    
    private UserProfileResponse buildUserProfile(User user) {
        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isActive(user.getIsActive());
        
        if (user.getStudent() != null) {
            Student student = user.getStudent();
            builder.studentId(student.getStudentId())
                   .name(student.getName())
                   .department(student.getDepartment())
                   .batchYear(student.getBatchYear())
                   .placementStatus(student.getPlacementStatus().name());
        }
        
        return builder.build();
    }
    /**
 * Register new admin
 */
@Transactional
public AuthResponse registerAdmin(RegisterRequest request) {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
        throw new IllegalArgumentException("Username already exists");
    }
    
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Email already registered");
    }
    
    User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .role(User.UserRole.ADMIN)
            .isActive(true)
            .build();
    
    user = userRepository.save(user);
    
    String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().name());
    
    return AuthResponse.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .expiresIn(jwtExpiration / 1000)
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole().name())
            .build();
}

/**
 * Refresh token
 */
public TokenResponse refreshToken(String oldToken) {
    if (!jwtTokenUtil.validateToken(oldToken)) {
        throw new RuntimeException("Invalid token");
    }
    
    String username = jwtTokenUtil.getUsernameFromToken(oldToken);
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
    String newToken = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().name());
    
    return TokenResponse.builder()
            .accessToken(newToken)
            .tokenType("Bearer")
            .expiresIn(jwtExpiration / 1000)
            .build();
}
}