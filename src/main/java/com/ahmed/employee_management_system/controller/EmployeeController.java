package com.ahmed.employee_management_system.controller;

import com.ahmed.employee_management_system.abstraction.EmployeeService;
import com.ahmed.employee_management_system.core.GlobalResponse;
import com.ahmed.employee_management_system.dtos.CreateEmployeeDTO;
import com.ahmed.employee_management_system.dtos.UpdateEmployeeDTO;
import com.ahmed.employee_management_system.entity.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<GlobalResponse<Page<Employee>>> getEmployees(
            @RequestParam(defaultValue = "0")
            int page,
            @Max(100)
            @RequestParam(defaultValue = "10")
            int size
    ) {
        Page<Employee> employees = employeeService.findAll(page, size);
        return new ResponseEntity<>(new GlobalResponse<>(employees), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<GlobalResponse<Employee>> getEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        return new ResponseEntity<>(new GlobalResponse<>(employee), HttpStatus.OK);
    }


    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<GlobalResponse<Employee>> updateEmployee(@PathVariable UUID employeeId, @RequestBody @Valid UpdateEmployeeDTO updateEmployeeDTO) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, updateEmployeeDTO);
        return new ResponseEntity<>(new GlobalResponse<>(updatedEmployee), HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<GlobalResponse<Employee>> create(@RequestBody @Valid CreateEmployeeDTO createEmployeeDTO) {
        Employee insertedEmployee = employeeService.createEmployee(createEmployeeDTO);
        return new ResponseEntity<>(new GlobalResponse<>(insertedEmployee), HttpStatus.CREATED);
    }


}
