package com.compassuol.sp.challenge.challenge3msuser.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDto {

    private String cep;
    private Long userId;

}
