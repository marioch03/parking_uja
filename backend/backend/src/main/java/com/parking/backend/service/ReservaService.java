package com.parking.backend.service;

import com.parking.backend.model.reserva;
import com.parking.backend.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Obtener todas las reservas
    public List<reserva> obtenerReservas() {
        return reservaRepository.findAll();
    }

    // Obtener una reserva por su ID
    public Optional<reserva> obtenerReservaPorId(int id) {
        return reservaRepository.findById(id);
    }

    // Guardar una nueva reserva
    public reserva guardarReserva(reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Eliminar una reserva
    public void eliminarReserva(int id) {
        reservaRepository.deleteById(id);
    }

    //obtener las reservasd de un usuario
    public List<reserva> obtener_reservas_usuario(int id_usuario){
        return reservaRepository.findByUsuarioId(id_usuario,Sort.by(Sort.Order.desc("fecha")));
    }
}

