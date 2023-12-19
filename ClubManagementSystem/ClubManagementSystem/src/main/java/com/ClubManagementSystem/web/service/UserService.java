package com.ClubManagementSystem.web.service;

import com.ClubManagementSystem.web.dto.RegistrationDto;
import com.ClubManagementSystem.web.models.UserEntity;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
}
