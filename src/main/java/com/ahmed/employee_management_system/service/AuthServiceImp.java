package com.ahmed.employee_management_system.service;

import com.ahmed.employee_management_system.abstraction.AuthService;
import com.ahmed.employee_management_system.config.JwtHelper;
import com.ahmed.employee_management_system.core.CustomResponseException;
import com.ahmed.employee_management_system.dtos.LoginRequestDTO;
import com.ahmed.employee_management_system.dtos.SignUpRequestDTO;
import com.ahmed.employee_management_system.entity.Employee;
import com.ahmed.employee_management_system.entity.UserAccount;
import com.ahmed.employee_management_system.repo.EmployeeRepo;
import com.ahmed.employee_management_system.repo.UserAccountRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements AuthService {

    //    @Autowired
    private final EmployeeRepo employeeRepo;

    //    @Autowired
    private final UserAccountRepo userAccountRepo;

    //    @Autowired
    private final PasswordEncoder passwordEncoder;

    //    @Autowired
    private final AuthenticationManager authenticationManager;

    //    @Autowired
    private final JwtHelper jwtHelper;

    @Override
    @Transactional
    public UserAccount signup(SignUpRequestDTO signUpRequestDTO, String token) {

        Employee employee = employeeRepo.findOneByAccountCreationToken(token).orElseThrow(() ->
                CustomResponseException.ResourceNotFound("employee with token " + token + " not found")
        );

        if (employee.isVerified()) {
            throw CustomResponseException.BadRequest("Account Already Verified");
        }

        UserAccount userAccount = new UserAccount();

        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername(signUpRequestDTO.username());
        userAccount.setPassword(passwordEncoder.encode(signUpRequestDTO.password()));
        userAccount.setEmployee(employee);
        userAccountRepo.save(userAccount);

        employee.setVerified(true);
        employee.setAccountCreationToken(null);
        employeeRepo.save(employee);
        return userAccount;
    }

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.username(),
                loginRequestDTO.password()
        ));
        UserAccount userAccount = userAccountRepo.findByUserName(loginRequestDTO.username()).orElseThrow(CustomResponseException::BadCredential);

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("userId", userAccount.getId());

        return jwtHelper.generateToken(claimsMap, userAccount);
    }
}


