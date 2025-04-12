package com.parking.backend.repository;

import com.parking.backend.model.estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<estado, Integer> {
    // Puedes agregar consultas personalizadas si es necesario
}


