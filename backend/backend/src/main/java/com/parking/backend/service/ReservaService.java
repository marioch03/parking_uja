package com.parking.backend.service;

import com.parking.backend.model.Estado;
import com.parking.backend.model.Plaza;
import com.parking.backend.model.Reserva;
import com.parking.backend.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private PlazaService plazaService;

    @Autowired
    private EstadoService estadoService;

    // Obtener todas las reservas
    public List<Reserva> obtenerReservas() {
        return reservaRepository.findAll();
    }

    // Obtener una reserva por su ID
    public Optional<Reserva> obtenerReservaPorId(int id) {
        return reservaRepository.findById(id);
    }

    // Guardar una nueva reserva
    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Eliminar una reserva
    public boolean eliminarReserva(int id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if(reserva.isPresent()){
            Plaza plaza = reserva.get().getPlaza();
            Optional<Estado> estadoLibre = estadoService.obtenerEstadoPorId(1); //Estado LIBRE
            plazaService.actualizarPlaza(plaza.getId(), estadoLibre.get());
            reservaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //obtener las reservasd de un usuario
    public List<Reserva> obtener_reservas_usuario(int id_usuario){
        return reservaRepository.findByUsuarioId(id_usuario,Sort.by(Sort.Order.desc("fecha")));
    }

    public List<Reserva> obtenerReservasPlazaFechaDesc(int idPlaza){
        return reservaRepository.findByPlazaIdOrderByFechaDesc(idPlaza);
    }

    public boolean comprobarPlazaReservada(int idPlaza, String matricula){
        List<Reserva> reservasPlaza = obtenerReservasPlazaFechaDesc(idPlaza);
        System.out.println("RESERVAS: "+reservasPlaza);
        if(reservasPlaza.isEmpty()){
            return false;
        }
        System.out.println("MATRICULA BBDD: "+reservasPlaza.get(0).getMatricula());
        System.out.println("MATRICULA: : "+matricula);
        if(reservasPlaza.get(0).getMatricula().equals(matricula)){
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 300000) // Ejecutar cada 5 min (300000 ms)
    public void verificarYExpirarReservas() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Reserva> reservas = reservaRepository.findAll();

        for (Reserva reserva : reservas) {
            LocalDateTime fechaReserva = reserva.getFecha();
            long segundosDiff = fechaReserva.until(ahora, ChronoUnit.SECONDS);
            //Si la reserva se hizo hace 1 hora, se elimina
            if(segundosDiff > 3600) {
                reservaRepository.delete(reserva);
                Plaza plaza = reserva.getPlaza();
                Optional<Estado> estadoLibre = estadoService.obtenerEstadoPorId(1); //Estado LIBRE
                plazaService.actualizarPlaza(plaza.getId(), estadoLibre.get());
            }
        }
    }
}

