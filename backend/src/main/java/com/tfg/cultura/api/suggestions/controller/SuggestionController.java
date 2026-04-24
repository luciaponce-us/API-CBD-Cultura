package com.tfg.cultura.api.suggestions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.cultura.api.suggestions.model.dto.SuggestionCreateRequest;
import com.tfg.cultura.api.suggestions.model.dto.SuggestionResponse;
import com.tfg.cultura.api.suggestions.service.SuggestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suggestions")
@Tag(name = "Suggestions", description = "Módulo de gestión de sugerencias")
public class SuggestionController {

    private final SuggestionService service;

    public SuggestionController(SuggestionService service){
        this.service = service;
    }

    @Operation(
        summary = "RF-08: Realizar sugerencias",
        description = "Como usuario registrado, quiero poder escribir sugerencias para que la Delegación de Cultura tenga en cuenta mis necesidades a la hora de mejorar sus servicios.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
        @PostMapping(value = "/create")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Sugerencia creada correctamente"),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
                        @ApiResponse(responseCode = "404", description = "Autor no encontrado")
        })
    public ResponseEntity<SuggestionResponse> create(@Valid @RequestBody SuggestionCreateRequest request) {
        SuggestionResponse response = service.create(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
    
}
