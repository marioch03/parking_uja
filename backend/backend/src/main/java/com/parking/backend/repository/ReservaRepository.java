package com.parking.backend.repository;

import com.parking.backend.model.reserva;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<reserva, Integer> {
     // Buscar reservas por usuario
     List<reserva> findByUsuarioId(Integer usuarioId,Sort sort);
}

