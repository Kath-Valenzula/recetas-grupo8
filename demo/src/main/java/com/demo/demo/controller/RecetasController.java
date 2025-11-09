package com.demo.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.models.Receta;
import com.demo.demo.service.RecetaService;

@RestController
@RequestMapping("/api/recetas")
public class RecetasController {

    private final RecetaService recetaService;

    public RecetasController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<Receta>> home() {
        return ResponseEntity.ok(recetaService.listarPopulares());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Receta>> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingredientes,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            @RequestParam(required = false) Boolean popular
    ) {
        return ResponseEntity.ok(recetaService.buscar(nombre, tipoCocina, ingredientes, paisOrigen, dificultad,popular));
    }

    @GetMapping("/{id}")
    // FIX: Avoid null id warnings from static analysis on path variables.
    public ResponseEntity<Receta> detalle(@PathVariable @NonNull Long id) {
        return recetaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    // FIX: Validate request body upfront to prevent null-handling warnings downstream.
    public ResponseEntity<Receta> crear(@RequestBody @NonNull Receta receta) {
        return ResponseEntity.ok(recetaService.guardar(receta));
    }
}
