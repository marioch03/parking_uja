package com.parking.backend.repository;

import com.parking.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsername(String username);
    Optional<Usuario> findByMail(String mail);
    boolean existsByUsername(String username);
    boolean existsByMail(String mail);
}