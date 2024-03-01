package com.compassuol.sp.challenge.challenge3msuser.serviceTest;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.entity.UserNotificationSend;
import com.compassuol.sp.challenge.challenge3msuser.repository.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.service.UserService;
import com.compassuol.sp.challenge.challenge3msuser.web.Client.AddressClient;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserAddressDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserCreateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.UserUpdateDto;
import com.compassuol.sp.challenge.challenge3msuser.web.mqueue.NotificationQueue;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private AddressClient addresClient;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationQueue notificationQueue;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserSuccessfully() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("John");
        userCreateDto.setLastName("Doe");
        userCreateDto.setCpf("12345678901");
        userCreateDto.setBirthdate(new Date());
        userCreateDto.setEmail("john.doe@example.com");
        userCreateDto.setPassword("password");
        userCreateDto.setActive(true);
        userCreateDto.setCep("12345678");

        User expectedUser = new User();
        expectedUser.setFirstName(userCreateDto.getFirstName());
        expectedUser.setLastName(userCreateDto.getLastName());
        expectedUser.setCpf(userCreateDto.getCpf());
        expectedUser.setBirthdate(userCreateDto.getBirthdate());
        expectedUser.setEmail(userCreateDto.getEmail());
        expectedUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        expectedUser.setActive(userCreateDto.getActive());
        expectedUser.setCep(userCreateDto.getCep());

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User actualUser = userService.createUser(userCreateDto);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).save(any(User.class));
        verify(notificationQueue, times(1)).sendUserCreateQueue(any(UserNotificationSend.class));
    }

    @Test
    public void createUserThrowsExceptionWhenNotificationFails() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("John");
        userCreateDto.setLastName("Doe");
        userCreateDto.setCpf("12345678901");
        userCreateDto.setBirthdate(new Date());
        userCreateDto.setEmail("john.doe@example.com");
        userCreateDto.setPassword("password");
        userCreateDto.setActive(true);
        userCreateDto.setCep("12345678");

        doThrow(new RuntimeException()).when(notificationQueue).sendUserCreateQueue(any(UserNotificationSend.class));

        try {
            userService.createUser(userCreateDto);
        } catch (RuntimeException e) {
            verify(userRepository, times(1)).save(any(User.class));
            verify(notificationQueue, times(1)).sendUserCreateQueue(any(UserNotificationSend.class));
        }
    }

    @Test
    public void getUserByIdReturnsUserWhenUserExists() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userService.getUserById(userId);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void getUserByIdReturnsEmptyWhenUserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.getUserById(userId);

        assertFalse(actualUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    public void updateUserThrowsExceptionWhenUserNotFound() throws Exception {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, userUpdateDto));
        verify(userRepository, times(0)).save(any(User.class));
        verify(notificationQueue, times(0)).sendUserCreateQueue(any(UserNotificationSend.class));
    }

    @Test
    public void updatePasswordSuccessfully() throws Exception {
        Long userId = 1L;
        String newPassword = "newPassword";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("john.doe@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.updatePassword(userId, newPassword);

        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(any(User.class));
        verify(notificationQueue, times(1)).sendUserCreateQueue(any(UserNotificationSend.class));
    }

    @Test
    public void updatePasswordThrowsExceptionWhenUserNotFound() throws Exception {
        Long userId = 1L;
        String newPassword = "newPassword";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(userId, newPassword));
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(0)).encode(newPassword);
        verify(userRepository, times(0)).save(any(User.class));
        verify(notificationQueue, times(0)).sendUserCreateQueue(any(UserNotificationSend.class));
    }


    @Test
    public void buscarPorUsernameReturnsUserWhenUserExists() {
        String username = "john.doe@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(username);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.buscarPorUsername(username);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    public void buscarPorUsernameThrowsExceptionWhenUserDoesNotExist() {
        String username = "john.doe@example.com";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.buscarPorUsername(username));
        verify(userRepository, times(1)).findByEmail(username);
    }
    @Test
    public void buscarRolePorUsernameReturnsRoleWhenUserExists() {
        String username = "john.doe@example.com";
        User.Role expectedRole = User.Role.ROLE_USER;

        User user = new User();
        user.setEmail(username);
        user.setRole(expectedRole);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        User.Role actualRole = userService.buscarRolePorUsername(username);

        assertEquals(expectedRole, actualRole);
        verify(userRepository, times(1)).findByEmail(username);
    }


    @Test
    public void getUserAddressReturnsAddressWhenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setCep("12345678");

        UserAddressDto expectedAddress = new UserAddressDto(user.getCep(), user.getId());

        when(addresClient.saveAddress(any(UserAddressDto.class))).thenReturn(expectedAddress);

        UserAddressDto actualAddress = userService.getUserAddress(user);

        assertEquals(expectedAddress, actualAddress);
        verify(addresClient, times(1)).saveAddress(any(UserAddressDto.class));
    }

    @Test
    public void getUserAddressThrowsExceptionWhenAddressNotFound() {
        User user = new User();
        user.setId(1L);
        user.setCep("12345678");

        when(addresClient.saveAddress(any(UserAddressDto.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> userService.getUserAddress(user));
        verify(addresClient, times(1)).saveAddress(any(UserAddressDto.class));
    }

}
