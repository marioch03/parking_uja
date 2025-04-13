class EstadoPlaza {
  final int id;
  final String nombre;

  EstadoPlaza({
    required this.id,
    required this.nombre,
  });

  // Método para verificar si el estado es libre
  bool get isLibre => id == 1;
  
  // Método para verificar si el estado es ocupado
  bool get isOcupada => id == 2;
  
  // Método para verificar si el estado es reservado
  bool get isReservada => id == 3;
}

// Estados disponibles
class EstadosPlaza {
  static final libre = EstadoPlaza(id: 1, nombre: 'LIBRE');
  static final ocupada = EstadoPlaza(id: 2, nombre: 'OCUPADA');
  static final reservada = EstadoPlaza(id: 3, nombre: 'RESERVADA');
} 