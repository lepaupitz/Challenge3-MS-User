package com.compassuol.sp.challenge.challenge3msuser.service;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.repository.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserCreateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public User createUser(UserCreateDto userCreateDto) {

        User CreatedUser = new User();
        CreatedUser.setFirstName(userCreateDto.getFirstName());
        CreatedUser.setLastName(userCreateDto.getLastName());
        CreatedUser.setCpf(userCreateDto.getCpf());
        CreatedUser.setBirthdate(userCreateDto.getBirthdate());
        CreatedUser.setEmail(userCreateDto.getEmail());
        CreatedUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        CreatedUser.setActive(userCreateDto.getActive());


        CreatedUser.setCep(userCreateDto.getCep());

        return userRepository.save(CreatedUser);

    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        updatedUser.setFirstName(userUpdateDto.getFirstName());
        updatedUser.setLastName(userUpdateDto.getLastName());
        updatedUser.setEmail(userUpdateDto.getEmail());
        updatedUser.setCpf(userUpdateDto.getCpf());
        updatedUser.setBirthdate(userUpdateDto.getBirthdate());
        updatedUser.setActive(userUpdateDto.isActive());

        return userRepository.save(updatedUser);
    }

    public void updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

    }

    @Transactional
    public User buscarPorUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario com '%s' não encontrado", username))
        );
    }

    @Transactional
    public User.Role buscarRolePorUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow().getRole();
    }
}
