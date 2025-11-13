package com.practica.aplicacionedafoclimatica.ui.screens.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.practica.aplicacionedafoclimatica.R
import com.practica.aplicacionedafoclimatica.ui.navigation.AppScreen
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel

@Composable
fun ConfigScreen(
    onConnectSuccess: () -> Unit,
    viewModel: SensorViewModel
) {
    // Definimos el color verde oscuro principal para mayor contraste
    val PrimaryGreen = Color(0xFF065F46)
    val LightGreen = Color(0xFF059669)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        // Color del borde cuando el campo NO está enfocado
        unfocusedBorderColor = PrimaryGreen.copy(alpha = 0.5f),
        // Color del borde cuando el campo SÍ está enfocado (más vivo)
        focusedBorderColor = LightGreen,
        // Color del texto que el usuario ingresa
        unfocusedTextColor = PrimaryGreen,
        focusedTextColor = PrimaryGreen,
        // Color de la etiqueta ("Dirección IP")
        unfocusedLabelColor = PrimaryGreen.copy(alpha = 0.7f),
        focusedLabelColor = LightGreen,
        // Color del cursor
        cursorColor = LightGreen
    )

    // Estados para los campos de texto
    var ip by remember { mutableStateOf("192.168.") } // IP por defecto
    var port by remember { mutableStateOf("5001") } // Puerto por defecto
    var urlPath by remember { mutableStateOf("") } // URL por defecto

    val status by viewModel.status.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Imagen de fondo (la misma que usas en otras pantallas)
        Image(
            painter = painterResource(id = R.drawable.wallpaper2),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.9f
        )

        // Tarjeta de configuración
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Configurar Conexión",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF065F46)
                )

                // Campo de Texto para IP
                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("Dirección IP") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = textFieldColors
                )

                // Campo de Texto para Puerto
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Puerto") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = urlPath,
                    onValueChange = { urlPath = it },
                    label = { Text("Ruta (ej: /api/datos)") },
                    placeholder = { Text("Dejar vacío para usar la raíz (/)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    singleLine = true,
                    colors = textFieldColors
                )

                // Texto de estado
                Text(
                    text = status,
                    fontSize = 14.sp,
                    color = Color(0xFF065F46)
                )

                // Botón Conectar
                Button(
                    onClick = {
                        // 1. Llama al ViewModel para establecer la conexión
                        viewModel.setConnection(ip = ip, port.toIntOrNull() ?: 5001, path = urlPath)

                        // 2. Navega a la pantalla principal
                        onConnectSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Conectar", color = Color.White)
                }
            }
        }
    }
}