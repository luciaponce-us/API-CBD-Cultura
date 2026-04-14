package com.tfg.cultura.api.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.cultura.api.users.model.dto.UserResponse;
import com.tfg.cultura.api.users.model.dto.UserRegisterRequest;
import com.tfg.cultura.api.users.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Módulo de gestión de usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
        summary = "RF-01: Registrar usuarios",
        description = "Como usuario, quiero poder solicitar mi registro en el sistema, para poder iniciar sesión.")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse user = userService.register(request);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(user);
        
    }
    
}
