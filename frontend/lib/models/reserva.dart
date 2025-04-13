import 'package:intl/intl.dart';
import 'plaza.dart';

class Reserva {
  final int id;
  final Plaza plaza;
  final DateTime fecha;
  final String matricula;

  Reserva({
    required this.id,
    required this.plaza,
    required this.fecha,
    required this.matricula,
  });

  factory Reserva.fromJson(Map<String, dynamic> json) {
    return Reserva(
      id: json['id'] as int,
      plaza: Plaza.fromJson(json['plaza'] as Map<String, dynamic>),
      fecha: DateTime.parse(json['fecha'] as String),
      matricula: json['matricula'].toString(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'plaza': plaza.toJson(),
      'fecha': DateFormat('yyyy-MM-ddTHH:mm:ss').format(fecha),
      'matricula': matricula,
    };
  }
}

// Lista temporal para almacenar las reservas (simulando base de datos)
List<Reserva> reservasUsuario = []; 