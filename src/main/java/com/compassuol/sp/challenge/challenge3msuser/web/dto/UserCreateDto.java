package com.compassuol.sp.challenge.challenge3msuser.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {


    @Size(min = 3)
    private String firstName;

    @Size(min = 3)
    private String lastName;

    @CPF
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private String birthdate;

    @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;

    private String cep;

    @Size(min = 6)
    private String password;

    private boolean active;
}
