package com.demo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.demo.demo.models.Receta;
import com.demo.demo.repository.RecetaRepository;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;

    public RecetaService(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    public List<Receta> listarTodas() {
        return recetaRepository.findAll();
    }

    public List<Receta> listarPopulares() {
        return recetaRepository.findByPopularTrue();
    }

    // FIX: Enforce non-null id to satisfy null-safety analysis.
    public Optional<Receta> obtenerPorId(@NonNull Long id) {
        return recetaRepository.findById(id);
    }

    // FIX: Ensure Receta payload is validated at the service boundary.
    public Receta guardar(@NonNull Receta receta) {
        return recetaRepository.save(receta);
    }

    public List<Receta> buscar(String nombre, String tipoCocina, String ingredientes, String pais, String dificultad,
            Boolean popular) {
        if (Boolean.TRUE.equals(popular)) {
            return recetaRepository.findByPopularTrue();
        }
        if (nombre != null && !nombre.isEmpty()) {
            return recetaRepository.findByNombreContainingIgnoreCase(nombre);
        }
        if (tipoCocina != null && !tipoCocina.isEmpty()) {
            return recetaRepository.findByTipoCocinaContainingIgnoreCase(tipoCocina);
        }
        if (ingredientes != null && !ingredientes.isEmpty()) {
            return recetaRepository.findByIngredientesContainingIgnoreCase(ingredientes);
        }
        if (pais != null && !pais.isEmpty()) {
            return recetaRepository.findByPaisOrigenContainingIgnoreCase(pais);
        }
        if (dificultad != null && !dificultad.isEmpty()) {
            return recetaRepository.findByDificultadContainingIgnoreCase(dificultad);
        }
        return recetaRepository.findAll();
    }
}
