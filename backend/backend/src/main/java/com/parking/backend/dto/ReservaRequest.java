package com.parking.backend.dto;

public class ReservaRequest {
    private int matricula;
    private int plaza;

    // Getters y Setters
    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getPlaza() {
        return plaza;
    }

    public void setPlaza(int plaza) {
        this.plaza = plaza;
    }
}
