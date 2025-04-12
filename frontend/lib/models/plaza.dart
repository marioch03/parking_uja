import 'estado_plaza.dart';

class Plaza {
  final int id;
  EstadoPlaza estado;

  Plaza({
    required this.id,
    required this.estado,
  });

  factory Plaza.fromJson(Map<String, dynamic> json) {
    return Plaza(
      id: json['id'],
      estado: _parseEstado(json['estadoId']),
    );
  }

  static EstadoPlaza _parseEstado(int estadoId) {
    switch (estadoId) {
      case 1:
        return EstadosPlaza.libre;
      case 2:
        return EstadosPlaza.ocupada;
      case 3:
        return EstadosPlaza.reservada;
      default:
        return EstadosPlaza.libre;
    }
  }
}

// Datos de ejemplo
List<Plaza> plazasIniciales = [
  Plaza(id: 1, estado: EstadosPlaza.libre),
  Plaza(id: 2, estado: EstadosPlaza.libre),
  Plaza(id: 3, estado: EstadosPlaza.libre),
  Plaza(id: 4, estado: EstadosPlaza.libre),
  Plaza(id: 5, estado: EstadosPlaza.libre),
]; 