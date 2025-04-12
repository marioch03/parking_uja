class EstadoPlaza {
  final int id;
  final String nombre;

  EstadoPlaza({
    required this.id,
    required this.nombre,
  });
}

// Estados disponibles
class EstadosPlaza {
  static final libre = EstadoPlaza(id: 1, nombre: 'LIBRE');
  static final ocupada = EstadoPlaza(id: 2, nombre: 'OCUPADA');
  static final reservada = EstadoPlaza(id: 3, nombre: 'RESERVADA');
} 