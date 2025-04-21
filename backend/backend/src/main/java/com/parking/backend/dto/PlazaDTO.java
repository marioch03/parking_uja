package com.parking.backend.dto;

import com.parking.backend.model.Estado;

public class PlazaDTO {
    private int id;
    private int numero;
    private int planta;
    private Estado estado;

    // Constructores
    public PlazaDTO() {
    }

    public PlazaDTO(int id, int numero, int planta, Estado estado) {
        this.id = id;
        this.numero = numero;
        this.planta = planta;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPlanta() {
        return planta;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
} 