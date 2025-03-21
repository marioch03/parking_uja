import 'estado_plaza.dart';

class Plaza {
  final int id;
  EstadoPlaza estado;

  Plaza({
    required this.id,
    required this.estado,
  });
}

// Datos de ejemplo
List<Plaza> plazasIniciales = [
  Plaza(id: 1, estado: EstadosPlaza.libre),
  Plaza(id: 2, estado: EstadosPlaza.libre),
  Plaza(id: 3, estado: EstadosPlaza.libre),
  Plaza(id: 4, estado: EstadosPlaza.libre),
  Plaza(id: 5, estado: EstadosPlaza.libre),
]; 