package com.example.employeeservice.service;

import com.example.employeeservice.abstraction.EmployeeService;
import com.example.employeeservice.dtos.CreateEmployeeDTO;
import com.example.employeeservice.dtos.EmployeeCreatedEvent;
import com.example.employeeservice.dtos.EmployeeResponseDTO;
import com.example.employeeservice.dtos.UpdateEmployeeDTO;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.gateway.DepartmentGateway;
import com.example.employeeservice.mapper.Mapper;
import com.example.employeeservice.repo.EmployeeRepo;

import core.CustomResponseException;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final DepartmentGateway departmentGateway;

    @Override
    public Page<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepo.findAll(pageable);
    }

    @Override
    public EmployeeResponseDTO findEmployeeById(UUID employeeId) {
        Employee employeeEntity = employeeRepo.findById(employeeId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                        "Employee with Id " + employeeId + " not found"
                ));

        return Mapper.toResponseDTO(employeeEntity);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO employee) {
        Employee updatedEmployee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound(
                        "Employee with Id " + employeeId + " not found"
                ));

        // Fixed validation logic
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

        // Handle Fallback/Unknown state from DepartmentGateway
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
        // Let JPA handle ID generation
        employee.setEmail(createEmployeeDTO.email());
        employee.setPosition(createEmployeeDTO.position());
        employee.setFirstName(createEmployeeDTO.firstName());
        employee.setLastName(createEmployeeDTO.lastName());
        employee.setHireAt(createEmployeeDTO.hireAt());
        employee.setPhoneNumber(createEmployeeDTO.phoneNumber());
        employee.setDepartmentId(response.data.id());

        Employee savedEmployee = employeeRepo.save(employee);

        // Transactional Outbox Pattern (using ApplicationEventPublisher + TransactionalEventListener)
        eventPublisher.publishEvent(new EmployeeCreatedEvent(
                savedEmployee.getEmail(),
                token
        ));

        return Mapper.toResponseDTO(savedEmployee);
    }
}
