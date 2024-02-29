package com.compassuol.sp.challenge.challenge3msuser.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName" ,nullable = false)
    private String firstName;

    @Column(name = "lastName" ,nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "birthdate",nullable = false)
    private Date birthdate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String cep;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "role", nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Getter
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }



}
