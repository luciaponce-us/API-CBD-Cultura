package com.tfg.cultura.api.users.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg.cultura.api.users.repository.UserRepository;

import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.*;

import com.tfg.cultura.api.users.exception.UserAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger logger = LoggerFactory.getLogger("usersLogger");

    public UserResponse register(UserRegisterRequest request) throws UserAlreadyExistsException {
        
        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Error al registrar el usuario {}: El nombre de usuario ya existe", request.getUsername());
            throw new UserAlreadyExistsException("El nombre de usuario ya existe"); 
        }

        if (userRepository.existsByDni(request.getDni())) {
            logger.warn("Error al registrar el usuario {}: El DNI {} ya existe", request.getUsername(), request.getDni());
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
}
