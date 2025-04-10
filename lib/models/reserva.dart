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

  factory Reserva.fromJson(Map<String, dynamic> json) {
    return Reserva(
      id: json['id'],
      usuario: json['usuario'],
      plaza: Plaza(
        id: json['plaza']['id'],
        estado: json['plaza']['estado'],
      ),
      fecha: DateTime.parse(json['fecha']),
      matricula: json['matricula'],
    );
  }
}

// Lista temporal para almacenar las reservas (simulando base de datos)
List<Reserva> reservasUsuario = []; 