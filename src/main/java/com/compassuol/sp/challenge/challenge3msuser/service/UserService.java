package com.compassuol.sp.challenge.challenge3msuser.service;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.entity.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(User user) {
        validateAndEncryptPassword(user);
        return userRepository.save(user);
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

    public void validateAndEncryptPassword(User user) {
        if (user.getPassword() != null && user.getPassword().length() > 6) {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }
        else {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }

}
