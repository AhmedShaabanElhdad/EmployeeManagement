package com.example.payrollservice.repo;

import com.example.payrollservice.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRepo extends JpaRepository<Payroll, UUID> {
    Optional<Payroll> findByEmployeeId(UUID employeeId);
}
