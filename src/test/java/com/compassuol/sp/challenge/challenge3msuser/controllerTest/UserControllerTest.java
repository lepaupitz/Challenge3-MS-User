package com.compassuol.sp.challenge.challenge3msuser.controllerTest;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import com.compassuol.sp.challenge.challenge3msuser.repository.UserRepository;
import com.compassuol.sp.challenge.challenge3msuser.service.UserService;
import com.compassuol.sp.challenge.challenge3msuser.web.controller.AuthenticationController;
import com.compassuol.sp.challenge.challenge3msuser.web.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private AuthenticationController authenticationController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MockMvc mockMvc;



    @Test
    public void createUserReturnsUserResponseDtoWhenUserCreatedSuccessfully() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("John");
        userCreateDto.setLastName("Doe");
        userCreateDto.setEmail("john.doe@example.com");
        userCreateDto.setPassword("password");
        userCreateDto.setCep("12345678");

        User createdUser = new User();
        createdUser.setFirstName(userCreateDto.getFirstName());
        createdUser.setLastName(userCreateDto.getLastName());
        createdUser.setEmail(userCreateDto.getEmail());
        createdUser.setPassword(userCreateDto.getPassword());
        createdUser.setCep(userCreateDto.getCep());

        UserAddressDto userAddressDto = new UserAddressDto();
        userAddressDto.setCep(userCreateDto.getCep());
        userAddressDto.setUserId(createdUser.getId());

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(createdUser);
        when(userService.getUserAddress(any(User.class))).thenReturn(userAddressDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto actualUserResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDto.class);

        assertEquals(userCreateDto.getFirstName(), actualUserResponseDto.getFirstName());
        assertEquals(userCreateDto.getLastName(), actualUserResponseDto.getLastName());
        assertEquals(userCreateDto.getEmail(), actualUserResponseDto.getEmail());
    }

    @Test
    public void createUserThrowsExceptionWhenUserCreationFails() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("John");
        userCreateDto.setLastName("Doe");
        userCreateDto.setEmail("john.doe@example.com");
        userCreateDto.setPassword("password");
        userCreateDto.setCep("12345678");

        when(userService.createUser(any(UserCreateDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void testUpdateUser() throws Exception {

        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("NovoNome");


        String jsonRequest = objectMapper.writeValueAsString(userUpdateDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();


        String jsonResponse = result.getResponse().getContentAsString();
        User updatedUser = objectMapper.readValue(jsonResponse, User.class);

        assertEquals(userUpdateDto.getFirstName(), updatedUser.getFirstName());

    }

    @Test
    public void getUserByIdReturnsUserWhenUserExists() throws Exception {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.of(expectedUser));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    public void getUserByIdReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUserReturnsUpdatedUserWhenUserExists() throws Exception {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("UpdatedName");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName(userUpdateDto.getFirstName());

        when(userService.updateUser(userId, userUpdateDto)).thenReturn(updatedUser);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andReturn();

        User actualUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertEquals(updatedUser.getFirstName(), actualUser.getFirstName());
    }

    @Test
    public void updateUserThrowsExceptionWhenUserUpdateFails() throws Exception {
        Long userId = 1L;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setFirstName("UpdatedName");

        when(userService.updateUser(userId, userUpdateDto)).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void updatePasswordReturnsOkWhenPasswordUpdatedSuccessfully() throws Exception {
        Long userId = 1L;
        String newPassword = "newPassword";

        doNothing().when(userService).updatePassword(userId, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePasswordThrowsExceptionWhenPasswordUpdateFails() throws Exception {
        Long userId = 1L;
        String newPassword = "newPassword";

        doThrow(new RuntimeException()).when(userService).updatePassword(userId, newPassword);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword))
                .andExpect(status().isInternalServerError());
    }

}
