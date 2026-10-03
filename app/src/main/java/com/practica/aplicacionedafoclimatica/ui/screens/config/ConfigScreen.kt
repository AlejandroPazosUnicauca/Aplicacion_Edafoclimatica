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

/**
 * Muestra la pantalla de configuración de conexión del sensor.
 *
 * @param onConnectSuccess Callback ejecutado cuando la conexión se inicializa correctamente.
 * @param viewModel ViewModel que almacena el estado de la conexión.
 */
@Composable
fun ConfigScreen(
    onConnectSuccess: () -> Unit,
    viewModel: SensorViewModel
) {
    val PrimaryGreen = Color(0xFF065F46)
    val LightGreen = Color(0xFF059669)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = PrimaryGreen.copy(alpha = 0.5f),
        focusedBorderColor = LightGreen,
        unfocusedTextColor = PrimaryGreen,
        focusedTextColor = PrimaryGreen,
        unfocusedLabelColor = PrimaryGreen.copy(alpha = 0.7f),
        focusedLabelColor = LightGreen,
        cursorColor = LightGreen
    )

    var ip by remember { mutableStateOf("192.168.") }
    var port by remember { mutableStateOf("5001") }
    var urlPath by remember { mutableStateOf("") }

    val status by viewModel.status.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper2),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.9f
        )

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

                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("Dirección IP") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = textFieldColors
                )

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

                Text(
                    text = status,
                    fontSize = 14.sp,
                    color = Color(0xFF065F46)
                )

                Button(
                    onClick = {
                        viewModel.setConnection(ip = ip, port.toIntOrNull() ?: 5001, path = urlPath)
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