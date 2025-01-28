import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Banco de Sangue'),
      ),
      body: Center(
        child: Text('Bem-vindo ao Banco de Sangue App!'),
      ),
    );
  }
}
