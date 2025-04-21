package com.parking.backend.service;

import com.parking.backend.model.Estado;
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
    public List<Estado> obtenerEstados() {
        return estadoRepository.findAll();
    }

    // Obtener un estado por su ID
    public Optional<Estado> obtenerEstadoPorId(int id) {
        return estadoRepository.findById(id);
    }

    // Guardar un nuevo Estado
    public Estado guardarEstado(Estado estado) {
        return estadoRepository.save(estado);
    }

    // Eliminar un estado
    public void eliminarEstado(int id) {
        estadoRepository.deleteById(id);
    }
}
