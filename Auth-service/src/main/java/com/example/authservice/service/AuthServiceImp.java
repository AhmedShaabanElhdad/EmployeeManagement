package com.example.authservice.service;

import com.example.authservice.abstraction.AuthService;
import com.example.authservice.client.EmployeeClient;
import com.example.authservice.dtos.*;
import com.example.authservice.entity.UserAccount;
import com.example.authservice.helper.JwtHelper;
import com.example.authservice.mapper.Mapper;
import com.example.authservice.repo.UserAccountRepo;
import core.CustomResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements AuthService {

    private final EmployeeClient employeeClient;
    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    @Override
    @Transactional
    public UserResponseDTO signup(SignUpRequestDTO signUpRequestDTO, String token) {

        EmployeeResponse employee = employeeClient.getEmployeeByToken(token);

        if (employee.verified()) {
            throw CustomResponseException.BadRequest("Account Already Verified");
        }

        if (userAccountRepo.findByUserName(signUpRequestDTO.username())
                .isPresent()) {

            throw CustomResponseException.BadRequest(
                    "userName already exists"
            );
        }

        UserAccount userAccount = new UserAccount();

        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername(signUpRequestDTO.username());
        userAccount.setPassword(passwordEncoder.encode(signUpRequestDTO.password()));
        userAccount.setEmployeeId(employee.employeeId());
        userAccountRepo.save(userAccount);

        log.info(
                "User created successfully: {}",
                signUpRequestDTO.username()
        );

        employeeClient.verify(new UserIdRequestDTO(userAccount.getEmployeeId().toString()));
        return Mapper.toUserResponseDTO(userAccount);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.username(),
                loginRequestDTO.password()
        ));

        UserAccount userAccount = userAccountRepo.findByUserName(loginRequestDTO.username()).orElseThrow(
                CustomResponseException::BadCredential
        );

        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("userId", userAccount.getId());
        accessClaims.put("role", userAccount.getRole());
        accessClaims.put("employeeId", userAccount.getEmployeeId());
        accessClaims.put("type", "access");

        String accessToken = jwtHelper.generateAccessToken(accessClaims, userAccount);

        Map<String, Object> refreshClaims =
                new HashMap<>();

        refreshClaims.put("userId", userAccount.getId());
        refreshClaims.put("type", "refresh");

        String refreshToken = jwtHelper.generateRefreshToken(refreshClaims, userAccount);

        return new AuthResponseDTO(
                accessToken,
                refreshToken
        );

    }

    @Override
    public AuthResponseDTO refresh(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String refreshToken = refreshTokenRequestDTO.refreshToken();
        String username = jwtHelper.extractUsername(refreshToken);
        UserAccount userAccount = userAccountRepo.findByUserName(username).orElseThrow(
                CustomResponseException::BadCredential
        );
        boolean valid = jwtHelper.isRefreshTokenValid(refreshToken, userAccount);
        if (!valid) {
            throw CustomResponseException.BadCredential();
        }

        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("userId", userAccount.getId());
        accessClaims.put("role", userAccount.getRole());
        accessClaims.put("employeeId", userAccount.getEmployeeId());
        accessClaims.put("type", "access");

        String accessToken = jwtHelper.generateAccessToken(accessClaims, userAccount);

        Map<String, Object> refreshClaims =
                new HashMap<>();

        refreshClaims.put("userId", userAccount.getId());
        refreshClaims.put("type", "refresh");

        String newRefreshToken = jwtHelper.generateRefreshToken(refreshClaims, userAccount);

        return new AuthResponseDTO(
                accessToken,
                newRefreshToken
        );
    }
}


