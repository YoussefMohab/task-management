package com.synctech.task_management_system.rest;

import com.synctech.task_management_system.dto.UserDTO;
import com.synctech.task_management_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody UserDTO userDTO) {
        // Register user using the service layer
        userService.registerUser(userDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return response;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        // Login user and generate tokens
        String tokens = userService.loginUser(username, password);
        Map<String, String> response = new HashMap<>();
        response.put("tokens", tokens);
        return response;
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestParam String refreshToken) {
        // Refresh token using the service layer
        String newTokens = userService.refreshToken(refreshToken);
        Map<String, String> response = new HashMap<>();
        response.put("tokens", newTokens);
        return response;
    }
}
