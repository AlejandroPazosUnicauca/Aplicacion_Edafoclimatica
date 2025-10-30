package com.practica.aplicacionedafoclimatica.screens.alerts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

val fechas = listOf("2023-05-01", "2023-05-02", "2023-05-03", "2023-05-04", "2023-05-05")

val tabla1 = "Es necesario hacer riego al cultivo." to listOf(
    "Temperatura (°C)" to listOf("24", "25", "26", "27", "28"),
    "Humedad (%)" to listOf("60", "58", "55", "59", "57")
)

val tabla2 = "pH óptimo" to listOf(
    "pH" to listOf("6.8", "7.0", "7.1", "6.9", "7.0")
)

val tabla3 = null to listOf(
    "N" to listOf("65", "63", "64", "66", "65"),
    "P" to listOf("30", "32", "31", "29", "30"),
    "K" to listOf("60", "62", "61", "59", "60")
)

@Composable
fun MainContentAlert(modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = com.practica.aplicacionedafoclimatica.R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SeccionTabla(
                titulo = "Temperatura y Humedad",
                descripcion = tabla1.first,
                datos = tabla1.second
            )
            Spacer(modifier = Modifier.height(24.dp))
            SeccionTabla(
                titulo = "pH",
                descripcion = tabla2.first,
                datos = tabla2.second
            )
            Spacer(modifier = Modifier.height(24.dp))
            SeccionTabla(
                titulo = "N, P, K",
                descripcion = tabla3.first,
                datos = tabla3.second
            )
        }
    }


}

@Composable
fun SeccionTabla(
    titulo: String,
    descripcion: String?,
    datos: List<Pair<String, List<String>>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFd1fae5) // Fondo verde claro
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF059669),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            descripcion?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    color = Color(0xFFFF5722),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
            TablaFechas(datos)
        }
    }
}

@Composable
fun TablaFechas(datos: List<Pair<String, List<String>>>) {
    val encabezados = listOf("Fecha") + fechas
    Row(modifier = Modifier.fillMaxWidth()) {
        encabezados.forEach { fecha ->
            Text(
                text = fecha,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF059669), // Verde fuerte
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
    datos.forEach { (nombre, valores) ->
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = nombre,
                color = Color(0xFF059669), // Verde fuerte
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            valores.forEach { valor ->
                Text(
                    text = valor,
                    color = Color(0xFF059669), // Verde fuerte
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}