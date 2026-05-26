package com.example.authservice.mapper;

import com.example.authservice.dtos.UserResponseDTO;
import com.example.authservice.entity.UserAccount;

public class Mapper {

    public static UserResponseDTO toUserResponseDTO(UserAccount userAccount) {
        return new UserResponseDTO(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getRole()
        );
    }
}
