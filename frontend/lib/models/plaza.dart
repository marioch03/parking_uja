import 'estado_plaza.dart';

class Plaza {
  final int id;
  EstadoPlaza estado;

  Plaza({
    required this.id,
    required this.estado,
  });

  factory Plaza.fromJson(Map<String, dynamic> json) {
    // El estado viene como un objeto completo desde el backend
    final estadoJson = json['estado'] as Map<String, dynamic>;
    
    return Plaza(
      id: json['id'] as int,
      estado: EstadoPlaza(
        id: estadoJson['id'] as int,
        nombre: estadoJson['nombre'] as String,
      ),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'estado': {
        'id': estado.id,
        'nombre': estado.nombre,
      },
    };
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