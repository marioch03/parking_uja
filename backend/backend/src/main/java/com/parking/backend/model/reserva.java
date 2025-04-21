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
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "plaza")
    private Plaza plaza;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name= "matricula")
    private String matricula;


    // Constructores
    public Reserva() {
    }

    public Reserva(Usuario usuario, Plaza plaza,LocalDateTime fecha,String matricula) {
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(Plaza plaza) {
        this.plaza = plaza;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", matricula='" + matricula + '\'' +
                ", usuario=" + (usuario != null ? usuario.getId() : "null") +
                ", plaza=" + (plaza != null ? plaza.getId() : "null") +
                '}';
    }
}


