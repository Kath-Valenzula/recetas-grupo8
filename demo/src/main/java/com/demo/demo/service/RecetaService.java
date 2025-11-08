package com.demo.demo.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Receta> obtenerPorId(Long id) {
        return recetaRepository.findById(id);
    }

    public Receta guardar(Receta receta) {
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
