package com.ahmed.employee_management_system.abstraction;

import com.ahmed.employee_management_system.dtos.CreateEmployeeDTO;
import com.ahmed.employee_management_system.dtos.UpdateEmployeeDTO;
import com.ahmed.employee_management_system.entity.Employee;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EmployeeService {
    Page<Employee> findAll(int page, int size);

    Employee findEmployeeById(UUID id);

    Employee updateEmployee(UUID id, UpdateEmployeeDTO updateEmployeeDTO);

    void deleteEmployee(UUID id);

    Employee createEmployee(@Valid CreateEmployeeDTO createEmployeeDTO);
}
