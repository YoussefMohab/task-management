package com.synctech.task_management_system.service;

import com.synctech.task_management_system.dto.TokenResponse;
import com.synctech.task_management_system.dto.UserDTO;
import com.synctech.task_management_system.entity.User;

public interface UserService {
    User registerUser(UserDTO userDTO);
    TokenResponse loginUser(String username, String password);
    String refreshToken(String refreshToken);
}
