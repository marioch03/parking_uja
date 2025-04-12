package com.parking.uja.controller;

import com.parking.uja.model.Usuario;
import com.parking.uja.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Usuario usuario = usuarioService.findByEmailAndPassword(email, password);
        
        if (usuario != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", "token-jwt-aqui"); // TODO: Implementar JWT
            response.put("userId", usuario.getId());
            response.put("nombre", usuario.getNombre());
            response.put("email", usuario.getEmail());
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
} 