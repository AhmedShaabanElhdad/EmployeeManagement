package com.ahmed.employee_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "user_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;
    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "role", length = 20)
    private ROLE role = ROLE.USER;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    enum ROLE {
        USER, ADMIN
    }
}
