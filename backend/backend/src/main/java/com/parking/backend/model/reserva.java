package com.parking.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private usuario usuario;

    @ManyToOne
    @JoinColumn(name = "plaza")
    private plaza plaza;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name= "matricula")
    private int matricula;


    // Constructores
    public reserva() {
    }

    public reserva(usuario usuario, plaza plaza,LocalDateTime fecha,int matricula) {
        this.fecha=fecha;
        this.matricula=matricula;
        this.usuario=usuario;
        this.plaza=plaza;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(usuario usuario) {
        this.usuario = usuario;
    }

    public plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(plaza plaza) {
        this.plaza = plaza;
    }
}


