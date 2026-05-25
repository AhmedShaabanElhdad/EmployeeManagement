package com.ahmed.employee_management_system.service;

import com.ahmed.employee_management_system.core.CustomResponseException;
import com.ahmed.employee_management_system.entity.UserAccount;
import com.ahmed.employee_management_system.repo.UserAccountRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserAccountRepo userAccountRepo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepo.findByUserName(username).orElseThrow(CustomResponseException::BadCredential);

        return User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .roles(String.valueOf(userAccount.getRole()))
                .build();
    }

}
