import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/login_response.dart';
import 'token_service.dart';
import 'api_service.dart';

class AuthService {
  Future<LoginResponse> login(String email, String password) async {
    try {
      final response = await ApiService.post('/auth/login', {
        'email': email,
        'password': password,
      });

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseData = jsonDecode(response.body);
        final loginResponse = LoginResponse.fromJson(responseData);
        
        // Guardar el token y datos del usuario
        await TokenService.saveToken(loginResponse.token);
        await TokenService.saveUserData(
          loginResponse.userId,
          loginResponse.nombre,
          loginResponse.email,
        );
        
        return loginResponse;
      } else {
        throw Exception('Error en el login: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Error al conectar con el servidor: $e');
    }
  }

  Future<void> logout() async {
    await TokenService.clearToken();
  }

  Future<bool> isLoggedIn() async {
    final token = await TokenService.getToken();
    return token != null;
  }
}