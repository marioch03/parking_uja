package com.parking.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.parking.backend.model.Usuario;
import com.parking.backend.model.Estado;
import com.parking.backend.model.Plaza;
import com.parking.backend.model.Reserva;
import com.parking.backend.service.UsuarioService;
import com.parking.backend.service.EstadoService;
import com.parking.backend.service.MqttService;
import com.parking.backend.service.PlazaService;
import com.parking.backend.service.ReservaService;
import com.parking.backend.dto.LoginRequest;
import com.parking.backend.dto.ReservaRequest;
import com.parking.backend.dto.ApiResponse;
import com.parking.backend.security.JwtTokenProvider;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
@Validated
public class BackendController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PlazaService plazaService;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MqttService mqttService;

    // Endpoints de autenticación
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<Usuario> user = usuarioService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (user.isPresent()) {
            // Crear un objeto con la información necesaria para el frontend
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.get().getId());
            userData.put("username", user.get().getUsername());
            userData.put("email", user.get().getMail());
            
            // Generar token JWT
            String token = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                user.get().getUsername(), 
                null
            ));
            userData.put("token", token);
            
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", userData));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        // Con JWT, el logout se maneja en el cliente simplemente eliminando el token
        // No necesitamos hacer nada en el servidor
        return ResponseEntity.ok(new ApiResponse(true, "Logout successful"));
    }

    //metodo para obtener los usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getUsers() {
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }
    

    //metodo para obtener el estado de las plazas
    @GetMapping("/plazas")
    public ResponseEntity<List<Plaza>> getPlazas() {
        return ResponseEntity.ok(plazaService.obtenerPlazas());
    }


    //Metodo para reservar una plaza
    @PostMapping("/reservar")
    public ResponseEntity<String> postReservar(@RequestBody ReservaRequest reserva, @RequestHeader("Authorization") String token) {
        String matricula = reserva.getMatricula();
        int plaza = reserva.getPlaza();

        // Obtener el usuario del token
        String username = tokenProvider.getUsernameFromJWT(token.substring(7));
        Optional<Usuario> user = usuarioService.obtenerUsuarioPorUsername(username);
        
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        //compruebo que la plaza existe y esta libre
        Optional<Plaza> plaza_a_reservar = plazaService.obtenerPlazaPorId(plaza);
        if(plaza_a_reservar.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plaza no encontrada");
        }
        if(plaza_a_reservar.get().getEstado().getId()!=1){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Plaza no libre");
        }

        //creo la reserva
        Reserva nueva_Reserva = new Reserva(user.get(), plaza_a_reservar.get(), LocalDateTime.now(), matricula);

        //la introduzco en la base de datos
        reservaService.guardarReserva(nueva_Reserva);
        Optional<Estado> estadoReservado = estadoService.obtenerEstadoPorId(3);
        plazaService.actualizarPlaza(plaza, estadoReservado.get());
        String topicLed = "Led_"+plaza_a_reservar.get().getId();
        mqttService.publishMessage(topicLed, String.valueOf(3));
        //devuelvo confirmacion
        return ResponseEntity.ok("Reserva recibida: matricula=" + matricula + ", plaza=" + plaza);
    }

    @GetMapping("/misreservas")
    public ResponseEntity<List<Reserva>> misreservas(@RequestHeader("Authorization") String token) {
        String username = tokenProvider.getUsernameFromJWT(token.substring(7));
        Optional<Usuario> user = usuarioService.obtenerUsuarioPorUsername(username);
        
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Reserva> lista_reservas = reservaService.obtener_reservas_usuario(user.get().getId());
        return ResponseEntity.ok(lista_reservas);
    }

    @DeleteMapping("/cancelarreserva/{id}")
    public ResponseEntity<ApiResponse> cancelarReserva(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Optional<Reserva> reserva = reservaService.obtenerReservaPorId(id);
        if(reserva.isPresent()){
            Plaza plazaReserva = reserva.get().getPlaza();
            if (reservaService.eliminarReserva(id)) {
                String topic = "Led_"+plazaReserva.getId();
                mqttService.publishMessage(topic, "1");
                return ResponseEntity.ok(new ApiResponse(true, "Reserva eliminada correctamente"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "No se encontró la reserva con ID: " + id));
    
    }
}
