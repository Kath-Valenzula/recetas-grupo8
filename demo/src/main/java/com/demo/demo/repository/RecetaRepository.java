package com.demo.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.demo.models.Receta;


public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByNombreContainingIgnoreCase(String nombre);
    List<Receta> findByTipoCocinaContainingIgnoreCase(String tipoCocina);
    List<Receta> findByIngredientesContainingIgnoreCase(String ingredientes);
    List<Receta> findByPaisOrigenContainingIgnoreCase(String paisOrigen);
    List<Receta> findByDificultadContainingIgnoreCase(String dificultad);

    List<Receta> findByPopularTrue();

}
