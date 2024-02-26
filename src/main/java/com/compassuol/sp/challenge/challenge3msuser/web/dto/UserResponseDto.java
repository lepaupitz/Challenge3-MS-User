package com.compassuol.sp.challenge.challenge3msuser.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String cpf;
    private String birthdate;
    private String email;
    private String cep;
    private String password;
    private boolean active;

}
