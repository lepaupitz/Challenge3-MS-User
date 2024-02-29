package com.compassuol.sp.challenge.challenge3msuser.web.controller;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.service.UserService;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserCreateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserResponseDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {

        User user = userService.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, UserResponseDto.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto){
        User user = userService.updateUser(id, userUpdateDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.updatePassword(id, newPassword);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }
}

