package com.ahmed.employee_management_system.service;

import com.ahmed.employee_management_system.abstraction.EmployeeService;
import com.ahmed.employee_management_system.core.CustomResponseException;
import com.ahmed.employee_management_system.dtos.CreateEmployeeDTO;
import com.ahmed.employee_management_system.dtos.UpdateEmployeeDTO;
import com.ahmed.employee_management_system.entity.Department;
import com.ahmed.employee_management_system.entity.Employee;
import com.ahmed.employee_management_system.repo.DepartmentRepo;
import com.ahmed.employee_management_system.repo.EmployeeRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    //    @Autowired
    private final EmployeeRepo employeeRepo;

    //    @Autowired
    private final DepartmentRepo departmentRepo;

    //    @Autowired
    private final EmailService emailService;

    @Override
    public Page<Employee> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepo.findAll(pageable);
    }

    @Override
    public Employee findEmployeeById(UUID employeeId) {
        Optional<Employee> employee = employeeRepo.findById(employeeId);
        return employee.orElseThrow(() -> CustomResponseException.ResourceNotFound(
                "Employee with Id " + employeeId + " not found"
        ));
    }

    @Override
    public Employee updateEmployee(UUID employeeId, UpdateEmployeeDTO employee) {
        Optional<Employee> existEmployee = employeeRepo.findById(employeeId);
        Employee updatedEmployee = existEmployee.orElseThrow(() ->
                CustomResponseException.ResourceNotFound("Employee with Id " + employeeId + " not found")
        );
        updatedEmployee.setFirstName(employee.firstName());
        updatedEmployee.setLastName(employee.lastName());
        updatedEmployee.setPosition(employee.position());
        updatedEmployee.setPhoneNumber(employee.phoneNumber());

        return employeeRepo.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(UUID employeeId) {
        employeeRepo.findById(employeeId).orElseThrow(() -> CustomResponseException.ResourceNotFound(
                "Employee with Id " + employeeId + " not found"
        ));
        employeeRepo.deleteById(employeeId);
    }

    @Transactional
    @Override
    public Employee createEmployee(CreateEmployeeDTO createEmployeeDTO) {

        Department department = departmentRepo.findById(createEmployeeDTO.departmentId()).orElseThrow(() ->
                CustomResponseException.ResourceNotFound("department with Id " + createEmployeeDTO.departmentId() + " not found")
        );

        Employee employee = new Employee();
        Employee savedEmployee = null;

        try {
            String token = UUID.randomUUID().toString();
            employee.setAccountCreationToken(token);
            employee.setVerified(false);

            employee.setId(UUID.randomUUID());
            employee.setEmail(createEmployeeDTO.email());
            employee.setPosition(createEmployeeDTO.position());
            employee.setFirstName(createEmployeeDTO.firstName());
            employee.setLastName(createEmployeeDTO.lastName());
            employee.setHireAt(createEmployeeDTO.hireAt());
            employee.setPhoneNumber(createEmployeeDTO.phoneNumber());
            employee.setDepartment(department);
            savedEmployee = employeeRepo.save(employee);
            emailService.sendMessage(employee.getEmail(), token);

        } catch (Exception ex) {
            throw CustomResponseException.BadRequest("Employee Creation Failed");
        }


        return savedEmployee;
    }
}


