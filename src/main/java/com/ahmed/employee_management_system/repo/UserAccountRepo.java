package com.ahmed.employee_management_system.repo;

import com.ahmed.employee_management_system.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUserName(String userName);
}
