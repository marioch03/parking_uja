package com.parking.backend.service;

import com.parking.backend.model.usuario;
import com.parking.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por su ID
    public Optional<usuario> obtenerUsuarioPorId(int id) {
        return usuarioRepository.findById(id);
    }

    // Obtener un usuario por su nombre de usuario
    public Optional<usuario> obtenerUsuarioPorUsername(String username) {
        return Optional.ofNullable(usuarioRepository.findByUsername(username));
    }

    // Autenticar usuario
    public Optional<usuario> authenticateUser(String username, String password) {
        Optional<usuario> userOpt = Optional.ofNullable(usuarioRepository.findByUsername(username));
        
        if (userOpt.isPresent()) {
            usuario user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return userOpt;
            }
        }
        return Optional.empty();
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void guardarUsuario(usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
