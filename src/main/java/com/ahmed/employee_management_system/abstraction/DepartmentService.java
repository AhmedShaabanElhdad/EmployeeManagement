package com.ahmed.employee_management_system.abstraction;

import com.ahmed.employee_management_system.entity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<Department> findAll();

    Department findDepartmentById(UUID id);

    Department updateDepartment(UUID id, String name);

    void deleteDepartment(UUID id);

    Department createDepartment(String name);
}
