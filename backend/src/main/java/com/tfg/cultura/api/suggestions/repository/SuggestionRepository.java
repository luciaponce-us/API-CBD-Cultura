package com.tfg.cultura.api.suggestions.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tfg.cultura.api.suggestions.model.Suggestion;

public interface SuggestionRepository extends MongoRepository<Suggestion,String> {
    List<Suggestion> findAllByOrderByTotalSupportersDesc();
}
