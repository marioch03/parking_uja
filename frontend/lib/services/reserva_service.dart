import 'dart:convert';
import '../models/reserva.dart';
import 'api_service.dart';

class ReservaService {
  Future<List<Reserva>> getReservasUsuario() async {
    try {
      final response = await ApiService.get('/reservas/usuario');
      
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        return data.map((json) => Reserva.fromJson(json)).toList();
      } else {
        throw Exception('Error al obtener reservas: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error al conectar con el servidor: $e');
    }
  }

  Future<Reserva> crearReserva(String plazaId, String matricula, DateTime fecha) async {
    try {
      final response = await ApiService.post('/reservas', {
        'plazaId': plazaId,
        'matricula': matricula,
        'fecha': fecha.toIso8601String(),
      });

      if (response.statusCode == 201) {
        return Reserva.fromJson(jsonDecode(response.body));
      } else {
        throw Exception('Error al crear reserva: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error al conectar con el servidor: $e');
    }
  }

  Future<void> cancelarReserva(String reservaId) async {
    try {
      final response = await ApiService.delete('/reservas/$reservaId');
      
      if (response.statusCode != 200) {
        throw Exception('Error al cancelar reserva: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error al conectar con el servidor: $e');
    }
  }
}