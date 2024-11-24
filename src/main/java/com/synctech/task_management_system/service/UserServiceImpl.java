package com.synctech.task_management_system.service;

import com.synctech.task_management_system.dto.UserDTO;
import com.synctech.task_management_system.entity.Role;
import com.synctech.task_management_system.entity.RoleName;
import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.repository.UserRepository;
import com.synctech.task_management_system.repository.RoleRepository;
import com.synctech.task_management_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(UserDTO userDTO) {
        // Check if the username or email already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        // Find or create the role
        Role role = roleRepository.findByName(userDTO.getRole())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(userDTO.getRole());
                    return roleRepository.save(newRole);
                });

        // Create new user entity
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.getRoles().add(role); // Add the role to the user

        return userRepository.save(user);
    }

    public String loginUser(String username, String password) {
        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Check password match
            if (passwordEncoder.matches(password, user.getPassword())) {
                String accessToken = jwtUtil.generateAccessToken(user.getId());
                String refreshToken = jwtUtil.generateRefreshToken(user.getId());
                return accessToken + " " + refreshToken; // or store them as needed
            }
        }
        throw new RuntimeException("Invalid username or password");
    }

    public String refreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String userId = jwtUtil.getUserIdFromToken(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(UUID.fromString(userId));
            String newRefreshToken = jwtUtil.generateRefreshToken(UUID.fromString(userId));
            return newAccessToken + " " + newRefreshToken;
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
