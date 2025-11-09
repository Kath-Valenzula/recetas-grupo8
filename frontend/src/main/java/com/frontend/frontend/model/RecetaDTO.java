package com.frontend.frontend.model;

import java.util.List;
import java.util.Objects;

// FIX: Minimal DTO required by frontend services/controllers to avoid raw map usage warnings.
public class RecetaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String instrucciones;
    private Integer tiempoPreparacion;
    private String dificultad;
    private String imagenUrl;
    private Boolean popular;
    private List<String> ingredientes;
    private String categoria;
    private Boolean vegana;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Integer getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(Integer tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getVegana() {
        return vegana;
    }

    public void setVegana(Boolean vegana) {
        this.vegana = vegana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecetaDTO recetaDTO = (RecetaDTO) o;
        return Objects.equals(id, recetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
