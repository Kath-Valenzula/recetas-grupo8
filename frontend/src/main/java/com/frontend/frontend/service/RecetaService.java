package com.frontend.frontend.service;

import com.frontend.frontend.TokenStore;
import com.frontend.frontend.model.RecetaDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/recetas";
    private final TokenStore tokenStore;

    public RecetaService(TokenStore tokenStore) {
        this.restTemplate = new RestTemplate();
        this.tokenStore = tokenStore;
    }

    public List<RecetaDTO> buscarRecetas(String nombre, String tipoCocina, String ingredientes, String paisOrigen, String dificultad,Boolean popular) {

        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/buscar")
                .queryParamIfPresent("nombre", Optional.ofNullable(nombre))
                .queryParamIfPresent("tipoCocina", Optional.ofNullable(tipoCocina))
                .queryParamIfPresent("ingredientes", Optional.ofNullable(ingredientes))
                .queryParamIfPresent("paisOrigen", Optional.ofNullable(paisOrigen))
                .queryParamIfPresent("dificultad", Optional.ofNullable(dificultad))
                .queryParamIfPresent("popular", Optional.ofNullable(popular))
                .toUriString();
        RecetaDTO[] recetas = restTemplate.getForObject(uri, RecetaDTO[].class);

        return Arrays.asList(recetas);
    }

    public RecetaDTO findById(Long id){
        String uri = baseUrl + "/" + id;
        String token = tokenStore.getToken();

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("El usuario no está autenticado o el token no está disponible.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RecetaDTO> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                RecetaDTO.class
        );

        return response.getBody();
    }
}
