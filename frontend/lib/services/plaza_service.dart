import 'dart:convert';
import '../models/plaza.dart';
import 'api_service.dart';

class PlazaService {
  Future<List<Plaza>> getPlazas() async {
    try {
      final response = await ApiService.get('/plazas');
      
      if (response.statusCode == 200) {
        final List<dynamic> data = jsonDecode(response.body);
        return data.map((json) => Plaza.fromJson(json)).toList();
      } else {
        throw Exception('Error al obtener plazas: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error al conectar con el servidor: $e');
    }
  }

  Future<Plaza> getPlazaById(String id) async {
    try {
      final response = await ApiService.get('/plazas/$id');
      
      if (response.statusCode == 200) {
        return Plaza.fromJson(jsonDecode(response.body));
      } else {
        throw Exception('Error al obtener plaza: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error al conectar con el servidor: $e');
    }
  }
} 