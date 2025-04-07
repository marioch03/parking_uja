import 'plaza.dart';

class Reserva {
  final int? id;
  final int usuario;
  final Plaza plaza;
  final DateTime fecha;
  final String matricula;

  Reserva({
    this.id,
    required this.usuario,
    required this.plaza,
    required this.fecha,
    required this.matricula,
  });
}

// Lista temporal para almacenar las reservas (simulando base de datos)
List<Reserva> reservasUsuario = []; 