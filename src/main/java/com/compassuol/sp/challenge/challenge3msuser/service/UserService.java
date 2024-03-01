package com.compassuol.sp.challenge.challenge3msuser.service;

import com.compassuol.sp.challenge.challenge3msuser.entity.NotificationMessage;
import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.entity.UserNotificationSend;
import com.compassuol.sp.challenge.challenge3msuser.repository.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.web.Client.AddressClient;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserAddressDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserCreateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserUpdateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.mqueue.NotificationQueue;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final AddressClient addresClient;

    private final NotificationQueue notificationQueue;

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
        userRepository.save(CreatedUser);

        UserNotificationSend userNotificationSend = new UserNotificationSend();
        userNotificationSend.setEmail(userCreateDto.getEmail());
        userNotificationSend.setEvent(NotificationMessage.CREATE);
        userNotificationSend.setSendDate(new Date());

        try {
            notificationQueue.sendUserCreateQueue(userNotificationSend);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CreatedUser;
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

        UserNotificationSend userNotificationSend = new UserNotificationSend();
        userNotificationSend.setEmail(userUpdateDto.getEmail());
        userNotificationSend.setEvent(NotificationMessage.UPDATE);
        userNotificationSend.setSendDate(new Date());
        try {
            notificationQueue.sendUserCreateQueue(userNotificationSend);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(updatedUser);
    }

    public void updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));

        UserNotificationSend userNotificationSend = new UserNotificationSend();
        userNotificationSend.setEmail(user.getEmail());
        userNotificationSend.setEvent(NotificationMessage.UPDATE_PASSWORD);
        userNotificationSend.setSendDate(new Date());
        try {
            notificationQueue.sendUserCreateQueue(userNotificationSend);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public UserAddressDto getUserAddress(User user) {
       UserAddressDto userAddressDto = addresClient.saveAddress(new UserAddressDto(user.getCep(), user.getId()));
       return userAddressDto;
    }
}
