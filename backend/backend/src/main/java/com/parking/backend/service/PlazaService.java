package com.parking.backend.service;

import com.parking.backend.model.plaza;
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
    public List<plaza> obtenerPlazas() {
        return plazaRepository.findAll();
    }

    // Obtener una plaza por su ID
    public Optional<plaza> obtenerPlazaPorId(int id) {
        return plazaRepository.findById(id);
    }


    // Actualizar una plaza
    @Transactional
    public plaza actualizarPlaza(plaza plaza) {
        if (!plazaRepository.existsById(plaza.getId())) {
            throw new RuntimeException("Plaza no encontrada");
        }
        return plazaRepository.save(plaza);
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

