class LoginResponse {
  final String token;
  final String id;
  final String username;
  final String email;

  LoginResponse({
    required this.token,
    required this.id,
    required this.username,
    required this.email,
  });

  factory LoginResponse.fromJson(Map<String, dynamic> json) {
    final data = json['data'];
    return LoginResponse(
      token: data['token'],
      id: data['id'].toString(),
      username: data['username'],
      email: data['email'],
    );
  }
}