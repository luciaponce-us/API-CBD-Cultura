package com.tfg.cultura.api.users.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg.cultura.api.users.repository.UserRepository;

import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.*;

import com.tfg.cultura.api.users.exception.*;
import com.tfg.cultura.api.users.jwt.CustomUserDetails;
import com.tfg.cultura.api.users.jwt.CustomUserDetailsService;
import com.tfg.cultura.api.users.jwt.JwtService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private static final Logger logger = LoggerFactory.getLogger("usersLogger");

    public UserResponse register(UserRegisterRequest request) throws UserAlreadyExistsException {

        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Error al registrar el usuario {}: El nombre de usuario ya existe", request.getUsername());
            throw new UserAlreadyExistsException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByDni(request.getDni())) {
            logger.warn("Error al registrar el usuario {}: El DNI {} ya existe", request.getUsername(),
                    request.getDni());
            throw new UserAlreadyExistsException("Ya existe un usuario con el mismo DNI");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .dni(request.getDni())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado correctamente: {}", savedUser.getUsername());
        return new UserResponse(savedUser);
    }

    public String login(UserLoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isEmpty()) {
            logger.warn("Error al iniciar sesión: El usuario {} no existe", request.getUsername());
            throw new UserNotFoundException(
                    "El usuario con username " + request.getUsername() + " no existe");
        }
        User foundUser = user.get();
        if (!foundUser.isActive()) {
            logger.warn("Error al iniciar sesión: El usuario {} está desactivado", request.getUsername());
            throw new DisabledException("El usuario está desactivado");
        }

        if (!passwordEncoder.matches(request.getPassword(), foundUser.getPassword())) {
            logger.warn("Error al iniciar sesión: El usuario {} introdujo una contraseña incorrecta", request.getUsername());
            throw new BadCredentialsException("Credenciales inválidaaaas");
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getUsername());

        return jwtService.generateToken(
                userDetails.getUsername(),
                userDetails.getRole(),
                userDetails.getId());
    }
}
