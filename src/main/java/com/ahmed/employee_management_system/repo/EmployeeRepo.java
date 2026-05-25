package com.ahmed.employee_management_system.repo;

import com.ahmed.employee_management_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {
    Optional<Employee> findOneByAccountCreationToken(String token);

}
