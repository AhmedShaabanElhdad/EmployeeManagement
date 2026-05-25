package com.ahmed.employee_management_system.repo;

import com.ahmed.employee_management_system.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, UUID> {
}
