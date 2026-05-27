package com.example.employeeservice.service;

import com.example.employeeservice.abstraction.EmployeeService;
import com.example.employeeservice.dtos.*;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.entity.Outbox;
import com.example.employeeservice.gateway.DepartmentGateway;
import com.example.employeeservice.mapper.Mapper;
import com.example.employeeservice.repo.EmployeeRepo;
import com.example.employeeservice.repo.OutboxRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.CustomResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final OutboxRepo outboxRepo;
    private final DepartmentGateway departmentGateway;
    private final ObjectMapper objectMapper;

    @Override
    public Page<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepo.findAll(pageable);
    }

    @Override
    @Cacheable(value = "employees", key = "#employeeId")
    public EmployeeResponseDTO findEmployeeById(UUID employeeId) {
        log.info("Fetching employee from DB for ID: {}", employeeId);
        Employee employeeEntity = employeeRepo.findById(employeeId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                        "Employee with Id " + employeeId + " not found"
                ));

        return Mapper.toResponseDTO(employeeEntity);
    }

    @Override
    @Transactional
    @CachePut(value = "employees", key = "#employeeId")
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO employee) {
        Employee updatedEmployee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                        "Employee with Id " + employeeId + " not found"
                ));

        if (employeeRepo.existsByEmailAndIdNot(employee.email(), employeeId)) {
            throw CustomResponseException.BadRequest("Email already exists");
        }

        updatedEmployee.setFirstName(employee.firstName());
        updatedEmployee.setLastName(employee.lastName());
        updatedEmployee.setPosition(employee.position());
        updatedEmployee.setPhoneNumber(employee.phoneNumber());
        updatedEmployee.setEmail(employee.email());

        Employee employeeEntity = employeeRepo.save(updatedEmployee);
        return Mapper.toResponseDTO(employeeEntity);
    }

    @Transactional
    @Override
    @CacheEvict(value = "employees", key = "#employeeId")
    public void deleteEmployee(UUID employeeId) {
        if (!employeeRepo.existsById(employeeId)) {
            throw CustomResponseException.ResourceNotFound("Employee with Id " + employeeId + " not found");
        }
        employeeRepo.deleteById(employeeId);
    }

    @Transactional
    @Override
    public EmployeeResponseDTO createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        var response = departmentGateway.getDepartment(createEmployeeDTO.departmentId()).getBody();
        
        if (response == null || response.data == null || "UNKNOWN_DEPARTMENT".equals(response.data.name())) {
            throw CustomResponseException.ResourceNotFound("Department with Id " + createEmployeeDTO.departmentId() + " not found or unavailable");
        }

        if (employeeRepo.existsByEmail(createEmployeeDTO.email())) {
            throw CustomResponseException.BadRequest("Email already exists");
        }

        Employee employee = new Employee();
        String token = UUID.randomUUID().toString();
        employee.setAccountCreationToken(token);
        employee.setVerified(false);
        employee.setEmail(createEmployeeDTO.email());
        employee.setPosition(createEmployeeDTO.position());
        employee.setFirstName(createEmployeeDTO.firstName());
        employee.setLastName(createEmployeeDTO.lastName());
        employee.setHireAt(createEmployeeDTO.hireAt());
        employee.setPhoneNumber(createEmployeeDTO.phoneNumber());
        employee.setDepartmentId(response.data.id());

        Employee savedEmployee = employeeRepo.save(employee);

        // Transactional Outbox Pattern: Save event to Outbox table
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(savedEmployee.getEmail(), token);
        try {
            Outbox outbox = Outbox.builder()
                    .aggregateId(savedEmployee.getId().toString())
                    .aggregateType("Employee")
                    .eventType("EmployeeCreated")
                    .payload(objectMapper.writeValueAsString(event))
                    .createdAt(Instant.now())
                    .processed(false)
                    .build();
            outboxRepo.save(outbox);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize EmployeeCreatedEvent", e);
            throw new RuntimeException("Internal Server Error during event serialization");
        }

        return Mapper.toResponseDTO(savedEmployee);
    }

    @Override
    @Cacheable(value = "employees", key = "#token")
    public EmployeeResponse findByToken(String token) {
        Employee employee = employeeRepo.findOneByAccountCreationToken(token)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Employee not found"));

        return new EmployeeResponse(
                employee.getId(),
                employee.isVerified(),
                employee.getEmail()
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeResponse verifyEmployee(String userId) {
        Employee employee = employeeRepo.findById(UUID.fromString(userId))
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Employee not found"));

        employee.setVerified(true);
        employee.setAccountCreationToken(null);
        employeeRepo.save(employee);

        return new EmployeeResponse(
                employee.getId(),
                employee.isVerified(),
                employee.getEmail()
        );
    }
}
