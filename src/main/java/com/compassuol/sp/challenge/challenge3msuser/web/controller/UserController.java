package com.compassuol.sp.challenge.challenge3msuser.web.controller;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.service.UserService;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserAddressDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserCreateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserResponseDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    private final ModelMapper modelMapper;



    @Operation(
            summary = "Criar um novo usuário",
            description = "Recurso para criar um novo usuário",
            responses = { @ApiResponse(responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Dados inválidos do usuário fornecidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422",
                            description = "Campo(s) inválido(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {

        User user = userService.createUser(userCreateDto);
        UserAddressDto userAddressDto = userService.getUserAddress(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, UserResponseDto.class));
    }

    @Operation(summary = "Obter um usuário por ID",
            description = "Recurso para obter um usuário pelo seu ID",
            responses = {@ApiResponse(responseCode = "200",
                            description = "Usuário encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Atualizar um usuário por ID",
            description = "Recurso para atualizar um usuário pelo ID",
            responses = {@ApiResponse(responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Dados inválidos do usuário fornecidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto){
        User user = userService.updateUser(id, userUpdateDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Operation(summary = "Atualizar a senha de um usuário por ID",
            description = "Recurso para atualizar a senha de um usuário pelo seu ID",
            responses = {@ApiResponse(responseCode = "200",
                            description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Nova senha inválida fornecida",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))}
    )
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.updatePassword(id, newPassword);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }
}

