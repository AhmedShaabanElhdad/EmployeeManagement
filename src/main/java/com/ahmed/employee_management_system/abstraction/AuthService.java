package com.ahmed.employee_management_system.abstraction;

import com.ahmed.employee_management_system.dtos.LoginRequestDTO;
import com.ahmed.employee_management_system.dtos.SignUpRequestDTO;
import com.ahmed.employee_management_system.entity.UserAccount;
import jakarta.validation.Valid;

public interface AuthService {

    UserAccount signup(@Valid SignUpRequestDTO signUpRequestDTO, String token);

    String login(@Valid LoginRequestDTO loginRequestDTO);
}
