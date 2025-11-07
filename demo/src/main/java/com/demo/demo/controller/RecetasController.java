package com.demo.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Receta> detalle(@PathVariable Long id) {
        return recetaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Receta> crear(@RequestBody Receta receta) {
        return ResponseEntity.ok(recetaService.guardar(receta));
    }
}