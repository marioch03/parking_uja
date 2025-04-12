package com.parking.backend.repository;

import com.parking.backend.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<usuario, Integer> {
    usuario findByUsername(String username);
    Optional<usuario> findByMail(String mail);
    boolean existsByUsername(String username);
    boolean existsByMail(String mail);
}