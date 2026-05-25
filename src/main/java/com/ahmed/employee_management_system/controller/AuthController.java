package com.ahmed.employee_management_system.controller;

import com.ahmed.employee_management_system.abstraction.AuthService;
import com.ahmed.employee_management_system.core.GlobalResponse;
import com.ahmed.employee_management_system.dtos.LoginRequestDTO;
import com.ahmed.employee_management_system.dtos.SignUpRequestDTO;
import com.ahmed.employee_management_system.entity.UserAccount;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<UserAccount>> signUp(
            @RequestBody @Valid SignUpRequestDTO signUpRequestDTO,
            @RequestParam String token
    ) {
        UserAccount userAccount = authService.signup(signUpRequestDTO, token);
        return new ResponseEntity<>(new GlobalResponse<>(userAccount), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<String>> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO
    ) {
        String token = authService.login(loginRequestDTO);
        return new ResponseEntity<>(new GlobalResponse<>(token), HttpStatus.CREATED);
    }


}
