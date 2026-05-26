package com.example.authservice.dtos;

import com.example.authservice.entity.UserAccount;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        UserAccount.ROLE role
) {
}
