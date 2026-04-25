package com.tfg.cultura.api.suggestions.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.tfg.cultura.api.config.MockConfig;
import com.tfg.cultura.api.suggestions.model.Suggestion;
import com.tfg.cultura.api.suggestions.model.enumerators.SuggestionType;
import com.tfg.cultura.api.users.model.User;
import com.tfg.cultura.api.users.repository.UserRepository;

@SpringBootTest
@Import(MockConfig.class)
@ActiveProfiles("test")
class SuggestionRepositoryTest {

    @Autowired
    private SuggestionRepository repository;

    @Autowired
    private UserRepository userRepository;

    private Suggestion s1;
    private Suggestion s2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        userRepository.deleteAll();
        User userToSave = User.builder()
                .username("lucia")
                .password("12345678")
                .name("Lucía")
                .surname("García de Sola")
                .dni("33419630D")
                .phone("600123123")
                .email("lucia@test.com")
                .build();

        userRepository.save(userToSave);
        s1 = Suggestion.builder()
                .title("Test Suggestion Title 1")
                .description("Test Suggestion Description")
                .type(SuggestionType.EVENT)
                .authorId(userToSave.getId())
                .supportersId(List.of(userToSave.getId()))
                .totalSupporters(1)
                .build();
        
        s2 = Suggestion.builder()
                .title("Test Suggestion Title 2")
                .description("Test Suggestion Description")
                .type(SuggestionType.EVENT)
                .authorId(userToSave.getId())
                .build();

        repository.save(s1);
        repository.save(s2);
    }

    @Test
    void findAllByOrderByTotalSupportersDesc_return_sorted_suggestions(){
        List<Suggestion> res = repository.findAllByOrderByTotalSupportersDesc();
        assertEquals(2, res.size());
        assertSame(s1.getTotalSupporters(), res.get(0).getTotalSupporters());
        assertSame(s2.getTotalSupporters(), res.get(1).getTotalSupporters());
    }
}
