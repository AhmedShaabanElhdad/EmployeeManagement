package com.ahmed.employee_management_system.controller;

import com.ahmed.employee_management_system.abstraction.DepartmentService;
import com.ahmed.employee_management_system.core.GlobalResponse;
import com.ahmed.employee_management_system.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    public DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Department>>> getDepartment() {
        List<Department> departments = departmentService.findAll();
        return new ResponseEntity<>(new GlobalResponse<>(departments), HttpStatus.OK);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<GlobalResponse<Department>> getDepartment(@PathVariable UUID departmentId) {
        Department department = departmentService.findDepartmentById(departmentId);
        return new ResponseEntity<>(new GlobalResponse<>(department), HttpStatus.OK);
    }


    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID departmentId) {
        departmentService.deleteDepartment(departmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<GlobalResponse<Department>> updateDepartment(@PathVariable UUID departmentId, String name) {
        Department updatedDepartment = departmentService.updateDepartment(departmentId, name);
        return new ResponseEntity<>(new GlobalResponse<>(updatedDepartment), HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<GlobalResponse<Department>> create(String name) {
        Department insertedDepartment = departmentService.createDepartment(name);
        return new ResponseEntity<>(new GlobalResponse<>(insertedDepartment), HttpStatus.CREATED);
    }


}
