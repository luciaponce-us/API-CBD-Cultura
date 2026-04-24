package com.tfg.cultura.api.users.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;

import com.tfg.cultura.api.core.service.FileService;
import com.tfg.cultura.api.users.exception.UserAlreadyExistsException;
import com.tfg.cultura.api.users.exception.UserNotFoundException;
import com.tfg.cultura.api.users.jwt.CustomUserDetails;
import com.tfg.cultura.api.users.jwt.CustomUserDetailsService;
import com.tfg.cultura.api.users.jwt.JwtService;
import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.UserLoginRequest;
import com.tfg.cultura.api.users.model.dto.UserRegisterRequest;
import com.tfg.cultura.api.users.model.dto.UserResponse;
import com.tfg.cultura.api.users.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private UserService userService;

    private UserRegisterRequest register = new UserRegisterRequest();
    private User user = new User();
    private UserLoginRequest loginRequest = new UserLoginRequest();
    private static final MockMultipartFile AVATAR_FILE = new MockMultipartFile("avatar", "avatar.png", "image/png", "fake-image-content".getBytes());

    @BeforeEach
    void setUp() {
        register = UserRegisterRequest.builder()
                .username("testUsername")
                .password("testPassword")
                .name("TestName")
                .surname("TestSurname")
                .dni("74156106N")
                .phone("600123123")
                .email("test@example.com")
                .avatar(AVATAR_FILE)
                .build();

        user = User.builder()
                .id("user_id")
                .username("testUsername")
                .password("testPassword")
                .name("TestName")
                .surname("TestSurname")
                .dni("74156106N")
                .phone("600123123")
                .email("test@example.com")
                .avatar("https://res.cloudinary.com/dubz79y98/image/upload/v1776288595/avatar_placeholder_dreac3.png")
                .active(true)
                .build();

        loginRequest = UserLoginRequest.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

    }

    private void mockUserRegistration() {
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
    }

    @Test
    void should_return_user_response_when_registering_user() {
        mockUserRegistration();

        UserResponse response = userService.register(register);

        assertNotNull(response, "No se ha registrado el usuario");
        assertEquals(register.getUsername(), response.getUsername());
    }

    @Test
    void should_set_avatar_placeholder_when_registering_user_without_avatar() {
        register.setAvatar(null);
        mockUserRegistration();
        UserResponse response = userService.register(register);

        assertNotNull(response, "No se ha registrado el usuario");
        assertEquals(register.getUsername(), response.getUsername());
        assertEquals(UserService.AVATAR_PLACEHOLDER, response.getAvatar(), "No se ha asignado el avatar placeholder");
    }

    @Test
    void should_upload_avatar_when_registering_user_with_avatar(){
        mockUserRegistration();
        when(fileService.uploadFile(any())).thenReturn("url/avatar.png");

        UserResponse response = userService.register(register);

        assertNotNull(response, "No se ha registrado el usuario");
        assertNotNull(register.getAvatar());
        assertTrue(!register.getAvatar().isEmpty());
        assertEquals(register.getUsername(), response.getUsername());
        assertEquals("url/avatar.png", response.getAvatar());
    }

    @Test
    void should_throw_exception_when_registering_user_with_existing_username() {
        when(userRepository.existsByUsername(register.getUsername()))
                .thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(register));
        assertTrue(ex.getMessage().contains("nombre de usuario"));
    }

    @Test
    void should_throw_exception_when_registering_user_with_existing_dni() {
        when(userRepository.existsByDni(register.getDni()))
                .thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(register));
        assertTrue(ex.getMessage().contains("DNI"));
    }

    // UPLOAD AVATAR

    @Test
    void should_upload_avatar_successfully() {
        String userId = "123";
        when(fileService.uploadFile(any())).thenReturn("url/avatar.png");

        String result = userService.uploadAvatar(userId, AVATAR_FILE);

        assertEquals("url/avatar.png", result);
    }

    @Test
    void should_return_placeholder_when_upload_avatar_fails() {
        String userId = "123";
        when(fileService.uploadFile(any()))
                .thenThrow(new RuntimeException("error"));

        String result = userService.uploadAvatar(userId, AVATAR_FILE);

        assertEquals(UserService.AVATAR_PLACEHOLDER, result);
    }

    // LOGIN

    @Test
    void should_return_token_when_login_successfully() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(new CustomUserDetails(user));
        when(jwtService.generateToken(any(), any(), any())).thenReturn("test-token");

        String response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals("test-token", response);
    }

    @Test
    void should_throw_exception_when_login_with_unexisting_username() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.login(loginRequest));

        assertTrue(ex.getMessage().contains("no existe"));
    }

    @Test
    void should_throw_exception_when_login_with_disabled_user() {
        user.setActive(false);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        DisabledException ex = assertThrows(DisabledException.class, () -> userService.login(loginRequest));

        assertTrue(ex.getMessage().contains("desactivado"));
    }

    @Test
    void should_throw_exception_when_login_with_wrong_password() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> userService.login(loginRequest));

        assertTrue(ex.getMessage().contains("Credenciales inválidas"));
    }

}
