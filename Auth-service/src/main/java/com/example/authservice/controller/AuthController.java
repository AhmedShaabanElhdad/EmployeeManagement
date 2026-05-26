package com.example.authservice.controller;

import com.example.authservice.abstraction.AuthService;
import com.example.authservice.dtos.*;
import core.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<UserResponseDTO>> signUp(
            @RequestBody @Valid SignUpRequestDTO signUpRequestDTO,
            @RequestParam String token
    ) {
        UserResponseDTO userResponseDTO = authService.signup(signUpRequestDTO, token);
        return new ResponseEntity<>(new GlobalResponse<>(userResponseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<AuthResponseDTO>> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO
    ) {
        AuthResponseDTO responseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(new GlobalResponse<>(responseDTO));
    }


    @PostMapping("/refresh")
    public ResponseEntity<GlobalResponse<AuthResponseDTO>> refresh(
            @RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO
    ) {
        AuthResponseDTO responseDTO = authService.refresh(refreshTokenRequestDTO);
        return ResponseEntity.ok(new GlobalResponse<>(responseDTO));
    }

}
