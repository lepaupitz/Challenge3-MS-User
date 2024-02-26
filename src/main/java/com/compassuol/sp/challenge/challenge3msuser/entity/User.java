package com.compassuol.sp.challenge.challenge3msuser.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Users")
public class User {

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
    private String birthdate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String cep;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active")
    private boolean active;

}
