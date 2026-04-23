package com.tfg.cultura.api.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.tfg.cultura.api.config.exception.ApiErrorBuilder;
import com.tfg.cultura.api.config.exception.GlobalExceptionHandler;
import com.tfg.cultura.api.users.exception.UserAlreadyExistsException;
import com.tfg.cultura.api.users.exception.UsersExceptionHandler;
import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.UserRegisterRequest;
import com.tfg.cultura.api.users.model.dto.UserResponse;
import com.tfg.cultura.api.users.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

        private MockMvc mockMvc;

        private UserService userService;

        private static final String BASE_URL = "/api/users";
        private static final String REGISTER_URL = BASE_URL + "/register";

        private UserRegisterRequest userRegisterRequest;
        private UserResponse userResponse;
        ApiErrorBuilder apiErrorBuilder = new ApiErrorBuilder();

        @BeforeEach
        void setup() {
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();

                userService = Mockito.mock(UserService.class);

                UserController controller = new UserController(userService);

                mockMvc = MockMvcBuilders
                                .standaloneSetup(controller)
                                .setValidator(validator)
                                .setControllerAdvice(
                                                new GlobalExceptionHandler(apiErrorBuilder),
                                                new UsersExceptionHandler(apiErrorBuilder))
                                .build();

                userRegisterRequest = UserRegisterRequest.builder()
                                .username("test")
                                .password("12345678")
                                .name("John")
                                .surname("Doe")
                                .dni("12345678Z")
                                .phone("600123123")
                                .email("test@test.com")
                                .build();

                User createdUser = User.builder()
                                .username("test")
                                .password("12345678-encrypted")
                                .name("John")
                                .surname("Doe")
                                .dni("12345678Z")
                                .phone("600123123")
                                .email("test@test.com")
                                .build();

                userResponse = new UserResponse(createdUser);
        }

        @Test
        void register_success() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();

                when(userService.register(any())).thenReturn(userResponse);

                String userJson = objectMapper.writeValueAsString(userRegisterRequest);

                MockMultipartFile userPart = new MockMultipartFile(
                                "user",
                                "",
                                "application/json",
                                userJson.getBytes());

                mockMvc.perform(multipart(REGISTER_URL)
                                .file(userPart))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.username").value(userRegisterRequest.getUsername()));
        }

        @Test
        void register_fail_user_already_exists() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                UserAlreadyExistsException ex = new UserAlreadyExistsException("El nombre de usuario ya existe");

                when(userService.register(any())).thenThrow(ex);

                String userJson = objectMapper.writeValueAsString(userRegisterRequest);

                MockMultipartFile userPart = new MockMultipartFile(
                                "user",
                                "",
                                "application/json",
                                userJson.getBytes());

                mockMvc.perform(multipart(REGISTER_URL)
                                .file(userPart))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value(ex.getMessage()));
        }

        @Test
        void register_fail_invalid_data() throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();

                userRegisterRequest.setEmail("invalid-email");

                String userJson = objectMapper.writeValueAsString(userRegisterRequest);

                MockMultipartFile userPart = new MockMultipartFile(
                                "user",
                                "",
                                "application/json",
                                userJson.getBytes());

                mockMvc.perform(multipart(REGISTER_URL)
                                .file(userPart))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Campo email: El email no es válido"));
        }
}