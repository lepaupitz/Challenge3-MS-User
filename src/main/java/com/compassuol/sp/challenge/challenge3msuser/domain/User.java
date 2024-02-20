package com.compassuol.sp.challenge.challenge3msuser.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

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
    @Size(min = 3)
    private String firstName;

    @Column(name = "lastName" ,nullable = false)
    @Size(min = 3)
    private String lastName;

    @Column(nullable = false, unique = true)
    @CPF
    private String cpf;

    @Column(name = "birthdate",nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String birthdate;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String cep;

    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;

    @Column(name = "active")
    private boolean active;

}
