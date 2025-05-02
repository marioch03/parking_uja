package com.parking.backend.repository;

import com.parking.backend.model.Reserva;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
     // Buscar reservas por usuario
     List<Reserva> findByUsuarioId(Integer usuarioId,Sort sort);

     List<Reserva> findByPlazaId(Integer plazaId);

     // Buscar reservas por plaza, ordenadas por fecha (descendente)
    List<Reserva> findByPlazaIdOrderByFechaDesc(@Param("plazaId") Integer plazaId);
}

