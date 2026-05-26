package com.example.authservice.service;

import com.example.authservice.entity.UserAccount;
import com.example.authservice.repo.UserAccountRepo;
import core.CustomResponseException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountRepo userAccountRepo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepo.findByUserName(username)
                .orElseThrow(CustomResponseException::BadCredential);

        return User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .authorities(userAccount.getAuthorities())
                .disabled(!userAccount.isEnabled())
                .accountLocked(!userAccount.isAccountNonLocked())
                .build();
    }

}
