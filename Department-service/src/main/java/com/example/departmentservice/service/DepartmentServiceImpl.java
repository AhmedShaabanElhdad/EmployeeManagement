package com.example.departmentservice.service;

import com.example.departmentservice.abstraction.DepartmentService;
import com.example.departmentservice.dtos.CreateDepartmentRequest;
import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repo.DepartmentRepo;
import core.CustomResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;

    @Override
    @Cacheable(value = "departments_list")
    public List<Department> findAll() {
        log.info("Fetching all departments from DB");
        return departmentRepo.findAll();
    }

    @Override
    @Cacheable(value = "departments", key = "#departmentId")
    public Department findDepartmentById(UUID departmentId) {
        log.info("Fetching department from DB for ID: {}", departmentId);
        return departmentRepo.findById(departmentId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                "Department with Id " + departmentId + " not found"
        ));
    }

    @Override
    @Transactional
    @CachePut(value = "departments", key = "#departmentId")
    @CacheEvict(value = "departments_list", allEntries = true)
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
    @CacheEvict(value = {"departments", "departments_list"}, key = "#departmentId", allEntries = true)
    public void deleteDepartment(UUID departmentId) {
        if (!departmentRepo.existsById(departmentId)) {
            throw CustomResponseException.ResourceNotFound("Department with Id " + departmentId + " not found");
        }
        departmentRepo.deleteById(departmentId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "departments_list", allEntries = true)
    public Department createDepartment(CreateDepartmentRequest request) {
        Department department = new Department();
        department.setName(request.name());
        return departmentRepo.save(department);
    }
}
