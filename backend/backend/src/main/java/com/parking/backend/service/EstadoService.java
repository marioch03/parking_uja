package com.parking.backend.service;

import com.parking.backend.model.estado;
import com.parking.backend.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    // Obtener todos los estados
    public List<estado> obtenerEstados() {
        return estadoRepository.findAll();
    }

    // Obtener un estado por su ID
    public Optional<estado> obtenerEstadoPorId(int id) {
        return estadoRepository.findById(id);
    }

    // Guardar un nuevo estado
    public estado guardarEstado(estado estado) {
        return estadoRepository.save(estado);
    }

    // Eliminar un estado
    public void eliminarEstado(int id) {
        estadoRepository.deleteById(id);
    }
}
