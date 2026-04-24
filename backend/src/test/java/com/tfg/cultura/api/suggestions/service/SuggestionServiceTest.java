package com.tfg.cultura.api.suggestions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tfg.cultura.api.core.exception.UnathenticatedException;
import com.tfg.cultura.api.suggestions.model.Suggestion;
import com.tfg.cultura.api.suggestions.model.dto.SuggestionCreateRequest;
import com.tfg.cultura.api.suggestions.model.dto.SuggestionResponse;
import com.tfg.cultura.api.suggestions.model.enumerators.SuggestionType;
import com.tfg.cultura.api.suggestions.repository.SuggestionRepository;
import com.tfg.cultura.api.users.exception.UserNotFoundException;
import com.tfg.cultura.api.users.jwt.CustomUserDetails;
import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SuggestionServiceTest {

    @Mock
    private SuggestionRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SuggestionService service;

    private SuggestionCreateRequest request;
    private Suggestion suggestion;
    private User user;
    
    @BeforeEach
    void setUp() {
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

        suggestion = Suggestion.builder()
            .title("testTitle")
            .description("testDescription")
            .type(SuggestionType.CATALOG)
            .authorId(user.getId())
            .supportersId(List.of("2","3"))
            .totalSupporters(2)
            .build();
        
        request = SuggestionCreateRequest.builder()
            .title(suggestion.getTitle())
            .description(suggestion.getDescription())
            .type(suggestion.getType())
            .build();
    }

    private void mockAuthContext(){
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void should_return_suggestion_response_when_create_suggestion() {
        mockAuthContext();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(suggestion);

        SuggestionResponse response = service.create(request);

        assertNotNull(response);
        assertEquals(suggestion.getTitle(), response.getTitle());
        assertEquals(suggestion.getSupportersId().size(), response.getTotalSupporters());
    }

    @Test
    void should_throw_exception_when_user_not_authenticated() {
        try {
            service.create(request);
        } catch (Exception e) {
            assertEquals(e.getClass(), UnathenticatedException.class);
            assertTrue(e.getMessage().contains("autenticación"));
        }
    }

    @Test
    void should_throw_exception_when_user_has_no_authority() {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        try {
            service.create(request);
        } catch (Exception e) {
            assertEquals(e.getClass(), UnathenticatedException.class);
            assertTrue(e.getMessage().contains("información"));
        }
    }

    @Test
    void should_throw_exception_if_logged_user_not_exists() {
        mockAuthContext();

        try {
            service.create(request);
        } catch (Exception e) {
            assertEquals(e.getClass(), UserNotFoundException.class);
            assertTrue(e.getMessage().contains("logeado no existe"));
        }
    }

    @Test
    void getAll_should_return_sorted_suggestions() throws UserNotFoundException {
        when(repository.findAllByOrderByTotalSupportersDesc()).thenReturn(List.of(suggestion));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        List<SuggestionResponse> responses = service.getAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(suggestion.getTitle(), responses.get(0).getTitle());
        assertEquals(suggestion.getSupportersId().size(), responses.get(0).getTotalSupporters());
    }

    @Test
    void getAll_should_return_empty_list_if_no_suggestions() {
        when(repository.findAllByOrderByTotalSupportersDesc()).thenReturn(List.of());

        List<SuggestionResponse> responses = service.getAll();

        assertNotNull(responses);
        assertEquals(0, responses.size());
    }

    @Test
    void toResponse_should_return_null_if_author_not_exists() throws UserNotFoundException {
        when(repository.findAllByOrderByTotalSupportersDesc()).thenReturn(List.of(suggestion));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        List<SuggestionResponse> response = service.getAll();

        assertTrue(response.isEmpty());
    }




    


}
