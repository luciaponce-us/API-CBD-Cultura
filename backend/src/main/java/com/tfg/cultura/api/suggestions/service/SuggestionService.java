package com.tfg.cultura.api.suggestions.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tfg.cultura.api.core.exception.UnathenticatedException;
import com.tfg.cultura.api.suggestions.model.Suggestion;
import com.tfg.cultura.api.suggestions.model.dto.SuggestionCreateRequest;
import com.tfg.cultura.api.suggestions.model.dto.SuggestionResponse;
import com.tfg.cultura.api.suggestions.repository.SuggestionRepository;
import com.tfg.cultura.api.users.exception.UserNotFoundException;
import com.tfg.cultura.api.users.jwt.CustomUserDetails;
import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.model.dto.UserResponse;
import com.tfg.cultura.api.users.repository.UserRepository;

@Service
public class SuggestionService {

    private final SuggestionRepository repository;
    private final UserRepository userRepository;

    public SuggestionService(SuggestionRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger("suggestionsLogger");

    public SuggestionResponse create(SuggestionCreateRequest request)
            throws UnathenticatedException, UserNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            logger.warn("Error al crear la sugerencia: No se ha podido obtener la autenticación del usuario");
            throw new UnathenticatedException("No se ha podido obtener la autenticación del usuario");
        }
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        if (user == null) {
            logger.warn("Error al crear la sugerencia: No se ha podido obtener la información del usuario");
            throw new UnathenticatedException("No se ha podido obtener la información del usuario");
        }
        String loggedUserId = user.getId();
        if (!userRepository.existsById(loggedUserId)) {
            logger.warn("Error al crear la sugerencia: El usuario logeado no existe");
            throw new UserNotFoundException("El usuario logeado no existe");
        }
        Suggestion suggestion = Suggestion.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .authorId(loggedUserId)
                .build();

        Suggestion savedSuggestion = repository.save(suggestion);
        logger.info("Sugerencia creada con ID {} por el usuario con ID {}", savedSuggestion.getId(), loggedUserId);
        return toResponse(savedSuggestion);
    }

    private SuggestionResponse toResponse(Suggestion suggestion) throws UserNotFoundException {
        Optional<User> optionalAuthor = userRepository.findById(suggestion.getAuthorId());

        if (optionalAuthor.isEmpty()) {
            logger.warn("Error al convertir la sugerencia a respuesta: No se ha podido encontrar el autor con ID {}",
                    suggestion.getAuthorId());
            throw new UserNotFoundException("No se ha podido encontrar el autor de la sugerencia");
        }

        User author = optionalAuthor.get();

        UserResponse authorResponse = new UserResponse(author);

        List<User> supporters = userRepository.findAllById(
                suggestion.getSupportersId().stream().limit(3).toList());

        List<String> avatars = supporters.stream()
                .map(User::getAvatar)
                .toList();

        return new SuggestionResponse(suggestion, authorResponse, avatars);
    }

}
