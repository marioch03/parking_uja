package com.parking.backend.service;

import com.parking.backend.model.Usuario;
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
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por su ID
    public Optional<Usuario> obtenerUsuarioPorId(int id) {
        return usuarioRepository.findById(id);
    }

    // Obtener un usuario por su nombre de usuario
    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return Optional.ofNullable(usuarioRepository.findByUsername(username));
    }

    // Autenticar usuario
    public Optional<Usuario> authenticateUser(String username, String password) {
        Optional<Usuario> userOpt = Optional.ofNullable(usuarioRepository.findByUsername(username));
        
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return userOpt;
            }
        }
        return Optional.empty();
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
