package com.parking.backend.repository;

import com.parking.backend.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    // Puedes agregar consultas personalizadas si es necesario
}


