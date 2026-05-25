package com.ahmed.employee_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "employee",
        indexes = {
                @Index(name = "idx_employee_email", columnList = "email"),
                @Index(name = "idx_employee_department", columnList = "department_id")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "hire_at", nullable = false)
    private LocalDate hireAt;

    @Column(name = "phone_number", nullable = false, length = 25)
    private String phoneNumber;

    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean isVerified;

    @Column(name = "account_creation_token")
    private String accountCreationToken;

    @Column(name = "position", nullable = false)
    private String position;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
