package com.example.departmentservice.abstraction;


import com.example.departmentservice.dtos.CreateDepartmentRequest;
import com.example.departmentservice.entity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<Department> findAll();

    Department findDepartmentById(UUID id);

    Department updateDepartment(UUID id, String name);

    void deleteDepartment(UUID id);

    Department createDepartment(CreateDepartmentRequest request);
}
