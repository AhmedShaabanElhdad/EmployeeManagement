package com.example.departmentservice.service;

import com.example.departmentservice.abstraction.DepartmentService;
import com.example.departmentservice.dtos.CreateDepartmentRequest;
import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repo.DepartmentRepo;
import core.CustomResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;

    @Override
    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

    @Override
    public Department findDepartmentById(UUID departmentId) {
        return departmentRepo.findById(departmentId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                "Department with Id " + departmentId + " not found"
        ));
    }

    @Override
    @Transactional
    public Department updateDepartment(UUID departmentId, String name) {
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                        "Department with Id " + departmentId + " not found")
        );
        department.setName(name);
        return departmentRepo.save(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID departmentId) {
        if (!departmentRepo.existsById(departmentId)) {
            throw CustomResponseException.ResourceNotFound("Department with Id " + departmentId + " not found");
        }
        departmentRepo.deleteById(departmentId);
    }

    @Override
    @Transactional
    public Department createDepartment(CreateDepartmentRequest request) {
        Department department = new Department();
        department.setName(request.name());
        // ID is handled by @UuidGenerator in the Entity
        return departmentRepo.save(department);
    }
}
