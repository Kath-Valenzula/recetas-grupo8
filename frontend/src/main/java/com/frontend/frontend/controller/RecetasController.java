package com.frontend.frontend.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.frontend.frontend.model.RecetaDTO;
import com.frontend.frontend.service.RecetaService;

@Controller
@RequestMapping("/recetas")
public class RecetasController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public String listarRecetas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String ingredientes,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) String dificultad,
            Model model) {

        List<RecetaDTO> recetas = recetaService.buscarRecetas(nombre, tipoCocina, ingredientes, paisOrigen, dificultad,null);
        model.addAttribute("recetas", recetas);
        return "recetas";
    }

@GetMapping("/{id}")
public String detalleReceta(@PathVariable Long id, Model model) {

    RecetaDTO receta = recetaService.findById(id);
    
    model.addAttribute("nombre", receta.getNombre());
    model.addAttribute("ingredientes", receta.getIngredientes().split(",\\s*")); // convertir string en lista
    model.addAttribute("instrucciones", receta.getInstrucciones().split("\\;\\s*")); // separar por puntos si quieres pasos
    model.addAttribute("tiempo", receta.getTiempoPreparacion() + " minutos");
    model.addAttribute("dificultad", receta.getDificultad());
    model.addAttribute("imagenUrl", receta.getImagenUrl());
    model.addAttribute("popular", receta.getPopular());

    return "receta-detalle";
}

}
