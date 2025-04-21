package com.parking.backend.repository;

import com.parking.backend.model.Reserva;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
     // Buscar reservas por usuario
     List<Reserva> findByUsuarioId(Integer usuarioId,Sort sort);
}

