import 'package:flutter/material.dart';
import '../models/plaza.dart';
import '../models/estado_plaza.dart';
import '../models/reserva.dart';

class ConfirmarReservaScreen extends StatefulWidget {
  final Plaza plaza;

  const ConfirmarReservaScreen({
    super.key,
    required this.plaza,
  });

  @override
  State<ConfirmarReservaScreen> createState() => _ConfirmarReservaScreenState();
}

class _ConfirmarReservaScreenState extends State<ConfirmarReservaScreen> {
  final _matriculaController = TextEditingController();

  void _confirmarReserva() {
    if (_matriculaController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Por favor, introduce una matrícula'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    try {
      // Crear nueva reserva
      final nuevaReserva = Reserva(
        usuario: 1, // Por ahora hardcodeado, después vendrá del usuario logueado
        plaza: widget.plaza,
        fecha: DateTime.now(),
        matricula: _matriculaController.text.toUpperCase(),
      );

      // Añadir la reserva a la lista temporal
      reservasUsuario.add(nuevaReserva);

      // Actualizar el estado de la plaza
      setState(() {
        widget.plaza.estado = EstadosPlaza.reservada;
      });

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Plaza P-${widget.plaza.id} reservada con éxito'),
          backgroundColor: Theme.of(context).colorScheme.primary,
        ),
      );

      // Volvemos a la pantalla anterior
      Navigator.pop(context);
      Navigator.pop(context);
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Error al realizar la reserva: ${e.toString()}'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  @override
  void dispose() {
    _matriculaController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.primary,
        iconTheme: const IconThemeData(color: Colors.white),
        title: const Text(
          'Confirmar reserva',
          style: TextStyle(
            color: Colors.white,
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              Theme.of(context).colorScheme.primary.withOpacity(0.1),
              Colors.white,
            ],
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const Spacer(),
              // Tarjeta de información
              Card(
                elevation: 4,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(20.0),
                  child: Column(
                    children: [
                      // Icono del coche con fondo
                      Container(
                        padding: const EdgeInsets.all(20),
                        decoration: BoxDecoration(
                          color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                          shape: BoxShape.circle,
                        ),
                        child: Icon(
                          Icons.directions_car,
                          size: 60,
                          color: Theme.of(context).colorScheme.primary,
                        ),
                      ),
                      const SizedBox(height: 20),
                      // Información de la plaza
                      Text(
                        'Plaza P-${widget.plaza.id}',
                        style: const TextStyle(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 10),
                      Text(
                        'Introduce los datos de tu vehículo',
                        style: TextStyle(
                          fontSize: 16,
                          color: Colors.grey[600],
                        ),
                      ),
                      const SizedBox(height: 30),
                      // Campo de matrícula
                      TextField(
                        controller: _matriculaController,
                        textAlign: TextAlign.center,
                        textCapitalization: TextCapitalization.characters,
                        style: const TextStyle(
                          fontSize: 18,
                          letterSpacing: 2,
                        ),
                        decoration: InputDecoration(
                          hintText: 'Matrícula',
                          filled: true,
                          fillColor: Colors.grey[200],
                          prefixIcon: const Icon(Icons.credit_card),
                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(15),
                            borderSide: BorderSide.none,
                          ),
                          contentPadding: const EdgeInsets.symmetric(
                            horizontal: 20,
                            vertical: 15,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const Spacer(),
              // Botón de confirmar
              ElevatedButton(
                onPressed: _confirmarReserva,
                style: ElevatedButton.styleFrom(
                  backgroundColor: Theme.of(context).colorScheme.primary,
                  foregroundColor: Colors.white,
                  padding: const EdgeInsets.symmetric(vertical: 15),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(15),
                  ),
                  elevation: 2,
                ),
                child: const Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(Icons.check_circle_outline),
                    SizedBox(width: 8),
                    Text(
                      'CONFIRMAR',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        letterSpacing: 1,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }
} 