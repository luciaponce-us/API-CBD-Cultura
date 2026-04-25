package com.tfg.cultura.api.users.factory;

import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.UserLoginRequest;
import com.tfg.cultura.api.users.model.dto.UserRegisterRequest;
import com.tfg.cultura.api.users.model.dto.UserResponse;

public class UserFactory {

    public static User validUser() {
        return User.builder()
                .id("user_id")
                .username("test")
                .password("12345678-encrypted")
                .name("John")
                .surname("Doe")
                .dni("12345678Z")
                .phone("600123123")
                .email("test@test.com")
                .build();
    }

    public static UserResponse validUserResponse() {
        User user = validUser();
        return new UserResponse(user);
    }

    public static UserRegisterRequest validUserRegisterRequest() {
        User user = validUser();
        return UserRegisterRequest.builder()
                .username(user.getUsername())
                .password("12345678")
                .name(user.getName())
                .surname(user.getSurname())
                .dni(user.getDni())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    public static UserLoginRequest loginRequest() {
        User user = validUser();
        return UserLoginRequest.builder()
                .username(user.getUsername())
                .password("12345678")
                .build();
    }
}
