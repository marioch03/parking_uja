class LoginResponse {
  final String token;
  final String userId;
  final String nombre;
  final String email;

  LoginResponse({
    required this.token,
    required this.userId,
    required this.nombre,
    required this.email,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    return LoginResponse(
      token: json['token'],
      userId: json['userId'],
      nombre: json['nombre'],
      email: json['email'],
    );
  }
}