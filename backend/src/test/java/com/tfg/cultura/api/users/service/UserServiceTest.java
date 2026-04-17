package com.tfg.cultura.api.users.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;

import com.tfg.cultura.api.config.MockConfig;
import com.tfg.cultura.api.core.service.FileService;
import com.tfg.cultura.api.users.exception.UserAlreadyExistsException;
import com.tfg.cultura.api.users.jwt.CustomUserDetailsService;
import com.tfg.cultura.api.users.jwt.JwtService;
import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.UserRegisterRequest;
import com.tfg.cultura.api.users.model.dto.UserResponse;
import com.tfg.cultura.api.users.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Import(MockConfig.class)
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
                .avatar(new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[0]))
                .build();
    }

    private void mockUserRegistration(){
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void should_return_user_response_when_registering_user() {
        mockUserRegistration();

        UserResponse response = userService.register(register);
        
        assertNotNull(response, "No se ha registrado el usuario");
        assertEquals(register.getUsername(), response.getUsername());
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

}
