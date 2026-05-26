package com.example.departmentservice.service;

import com.example.departmentservice.abstraction.DepartmentService;
import com.example.departmentservice.dtos.CreateDepartmentRequest;
import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repo.DepartmentRepo;
import core.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DepartmentServiceImp implements DepartmentService {

    @Autowired
    DepartmentRepo departmentRepo;

    @Override
    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

    @Override
    public Department findDepartmentById(UUID departmentId) {
        Optional<Department> employee = departmentRepo.findById(departmentId);
        return employee.orElseThrow(() -> CustomResponseException.ResourceNotFound(
                "department with Id " + departmentId + " not found"
        ));
    }

    @Override
    public Department updateDepartment(UUID employeeId, String name) {
        Optional<Department> existDepartment = departmentRepo.findById(employeeId);
        Department updatedDepartment = existDepartment.orElseThrow(() ->
                CustomResponseException.ResourceNotFound("Department with Id " + employeeId + " not found")
        );
        updatedDepartment.setName(name);
        return departmentRepo.save(updatedDepartment);
    }

    @Override
    public void deleteDepartment(UUID departmentId) {
        departmentRepo.findById(departmentId).orElseThrow(() -> CustomResponseException.ResourceNotFound(
                "Department with Id " + departmentId + " not found"
        ));
        departmentRepo.deleteById(departmentId);
    }

    @Override
    public Department createDepartment(CreateDepartmentRequest request) {
        Department department = new Department();
        department.setId(UUID.randomUUID());
        department.setName(request.name());
        return departmentRepo.save(department);
    }
}


