package com.parking.backend.service;

import com.parking.backend.model.Estado;
import com.parking.backend.model.Plaza;
import com.parking.backend.repository.PlazaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlazaService {

    @Autowired
    private PlazaRepository plazaRepository;

    // Obtener todas las plazas
    public List<Plaza> obtenerPlazas() {
        return plazaRepository.findAll();
    }

    // Obtener una plaza por su ID
    public Optional<Plaza> obtenerPlazaPorId(int id) {
        return plazaRepository.findById(id);
    }


    // Actualizar una plaza
    @Transactional
    public Plaza actualizarPlaza(int id, Estado estado) {
        Optional<Plaza> plaza = plazaRepository.findById(id);
        if (plaza.isPresent()) {
            plaza.get().setEstado(estado);
            return plazaRepository.save(plaza.get());
        }
        return null;
    }

    // Eliminar una plaza
    @Transactional
    public void eliminarPlaza(int id) {
        if (!plazaRepository.existsById(id)) {
            throw new RuntimeException("Plaza no encontrada");
        }
        plazaRepository.deleteById(id);
    }
}

