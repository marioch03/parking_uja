import 'package:flutter/material.dart';
import 'package:parking_uja/models/plaza.dart';
import 'package:parking_uja/services/plaza_service.dart';
import 'package:parking_uja/screens/confirmar_reserva_screen.dart';

import '../models/estado_plaza.dart';

class ReservarPlazaScreen extends StatefulWidget {
  const ReservarPlazaScreen({super.key});

  @override
  State<ReservarPlazaScreen> createState() => _ReservarPlazaScreenState();
}

class _ReservarPlazaScreenState extends State<ReservarPlazaScreen> {
  final PlazaService _plazaService = PlazaService();
  List<Plaza> plazas = [];
  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _cargarPlazas();
  }

  Future<void> _cargarPlazas() async {
    try {
      setState(() {
        _isLoading = true;
        _error = null;
      });

      final plazasObtenidas = await _plazaService.getPlazas();
      
      if (mounted) {
        setState(() {
          plazas = plazasObtenidas;
          _isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _error = e.toString();
          _isLoading = false;
        });
      }
    }
  }

  void _reservarPlaza(Plaza plaza) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ConfirmarReservaScreen(plaza: plaza),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.primary,
        iconTheme: const IconThemeData(color: Colors.white),
        title: const Text(
          'Reservar plaza',
          style: TextStyle(
            color: Colors.white,
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        'Error: $_error',
                        style: const TextStyle(color: Colors.red),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton(
                        onPressed: _cargarPlazas,
                        child: const Text('Reintentar'),
                      ),
                    ],
                  ),
                )
              : ListView.builder(
                  padding: const EdgeInsets.all(16.0),
                  itemCount: plazas.length,
                  itemBuilder: (context, index) {
                    final plaza = plazas[index];
                    return Padding(
                      padding: const EdgeInsets.only(bottom: 16.0),
                      child: Card(
                        elevation: 2,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(15),
                          side: BorderSide(
                            color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                            width: 1,
                          ),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.all(16.0),
                          child: Row(
                            children: [
                              // Círculo con número de plaza
                              Container(
                                width: 50,
                                height: 50,
                                decoration: BoxDecoration(
                                  shape: BoxShape.circle,
                                  color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                                ),
                                child: Center(
                                  child: Text(
                                    'P-${plaza.id}',
                                    style: TextStyle(
                                      fontSize: 18,
                                      fontWeight: FontWeight.bold,
                                      color: Theme.of(context).colorScheme.primary,
                                    ),
                                  ),
                                ),
                              ),
                              const SizedBox(width: 16),
                              // Estado de la plaza
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      'Plaza ${plaza.id}',
                                      style: const TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.w500,
                                      ),
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      'Estado: ${plaza.estado.nombre}',
                                      style: TextStyle(
                                        fontSize: 14,
                                        color: plaza.estado == EstadosPlaza.ocupada
                                            ? Colors.red
                                            : plaza.estado == EstadosPlaza.reservada
                                                ? Colors.orange
                                                : Colors.green,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                              // Botón de reserva
                              if (plaza.estado == EstadosPlaza.libre)
                                ElevatedButton(
                                  onPressed: () => _reservarPlaza(plaza),
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Theme.of(context).colorScheme.primary,
                                    foregroundColor: Colors.white,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(10),
                                    ),
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 20,
                                      vertical: 12,
                                    ),
                                  ),
                                  child: const Text(
                                    'RESERVAR',
                                    style: TextStyle(
                                      fontSize: 14,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                            ],
                          ),
                        ),
                      ),
                    );
                  },
                ),
    );
  }
} 