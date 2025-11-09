package com.frontend.frontend.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.frontend.frontend.TokenStore;
import com.frontend.frontend.model.RecetaDTO;

@Service
public class RecetaService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/recetas";
    private final TokenStore tokenStore;

    public RecetaService(TokenStore tokenStore) {
        this.restTemplate = new RestTemplate();
        this.tokenStore = Objects.requireNonNull(tokenStore, "tokenStore");
    }

    public List<RecetaDTO> buscarRecetas(String nombre, String tipoCocina, String ingredientes, String paisOrigen,
            String dificultad, Boolean popular) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/buscar");
        if (nombre != null) {
            builder.queryParam("nombre", nombre);
        }
        if (tipoCocina != null) {
            builder.queryParam("tipoCocina", tipoCocina);
        }
        if (ingredientes != null) {
            builder.queryParam("ingredientes", ingredientes);
        }
        if (paisOrigen != null) {
            builder.queryParam("paisOrigen", paisOrigen);
        }
        if (dificultad != null) {
            builder.queryParam("dificultad", dificultad);
        }
        if (popular != null) {
            builder.queryParam("popular", popular);
        }

        String uri = builder.toUriString();
        RecetaDTO[] recetas = restTemplate.getForObject(uri, RecetaDTO[].class);

        return recetas != null ? Arrays.asList(recetas) : Collections.emptyList();
    }

    public RecetaDTO findById(Long id) {
        String uri = baseUrl + "/" + id;
        String token = Optional.ofNullable(tokenStore.getToken())
                .filter(t -> !t.isEmpty())
                // FIX: Validate token presence to avoid null warnings.
                .orElseThrow(() -> new IllegalStateException("El usuario no esta autenticado o el token no esta disponible."));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        HttpMethod method = safeMethod(HttpMethod.GET.name());

        ResponseEntity<RecetaDTO> response = restTemplate.exchange(
                uri,
                method,
                entity,
                RecetaDTO.class
        );

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new IllegalStateException("La respuesta del servicio de recetas no contiene datos."));
    }

    private @NonNull HttpMethod safeMethod(@NonNull String methodStr) {
        // FIX: Current Spring version lacks HttpMethod.resolve; emulate it via valueOf with null guard.
        HttpMethod resolved;
        try {
            resolved = HttpMethod.valueOf(methodStr);
        } catch (IllegalArgumentException ex) {
            resolved = null;
        }
        return Objects.requireNonNull(resolved, "HttpMethod invalido: " + methodStr);
    }
}
