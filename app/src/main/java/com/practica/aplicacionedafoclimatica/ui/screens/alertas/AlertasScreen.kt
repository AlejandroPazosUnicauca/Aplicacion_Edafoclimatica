package com.practica.aplicacionedafoclimatica.ui.screens.alertas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica.aplicacionedafoclimatica.data.model.SensorData
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel
// Importamos la lógica de rangos y estados de MedidasScreen
import com.practica.aplicacionedafoclimatica.ui.screens.medidas.* /**
 * Data class para almacenar la información de una alerta filtrada.
 */
data class AlertaInfo(
    val variable: String,
    val valorActual: String,
    val recomendacion: String,
    val estado: EstadoValor // Para colorear el ícono
)

/**
 * Función composable que filtra los datos del sensor y devuelve solo las alertas activas.
 */
@Composable
fun obtenerAlertas(latestData: SensorData?): List<AlertaInfo> {
    val alertas = remember(latestData) {
        val listaAlertas = mutableListOf<AlertaInfo>()
        if (latestData == null) return@remember emptyList<AlertaInfo>()

        // Convertimos los valores
        val lumenes = latestData.Lumenes.toFloat()
        val tempAmbiente = latestData.Temperatura_ambiente.toFloat()
        val humedadAmbiente = latestData.Humedad_ambiente.toFloat()
        val humedadSuelo = latestData.Humedad_suelo.toFloat()
        val ph = latestData.Ph
        val fosforo = latestData.Fosforo.toFloat()
        val nitrogeno = latestData.Nitrogeno.toFloat()
        val potasio = latestData.Potasio.toFloat()

        // --- Lógica de Alertas Basada en Recomendaciones ---

        // 1. Lúmenes (Lux > 20000)
        // Usamos optimoEnd de rangosLumenes que es 20000f
        if (lumenes > rangosLumenes.optimoEnd) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "Iluminación Excesiva",
                    valorActual = "${String.format("%.1f", lumenes)} Lux",
                    recomendacion = "Lux > 20000: Recomendar sombra parcial.",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        // 2. Humedad del Suelo (> 85%)
        // Usamos optimoEnd de rangosHumedadSuelo que es 85f
        if (humedadSuelo > rangosHumedadSuelo.optimoEnd) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "Exceso de Humedad (Suelo)",
                    valorActual = "${String.format("%.1f", humedadSuelo)}%",
                    recomendacion = "Humedad del suelo > 85%: Evaluar drenaje.",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        // 3. Riesgo de Roya (Combinada)
        // HR > 85% + temp > 21.5 °C
        if (humedadAmbiente > rangosHumedadAmbiente.optimoEnd && tempAmbiente > rangosTempAmbiente.optimoEnd) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "Riesgo de Roya",
                    valorActual = "HR: ${humedadAmbiente}% | Temp: ${tempAmbiente}°C",
                    recomendacion = "HR > 85% y Temp > 21.5°C: Aumentar monitoreo y ventilación.",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        // 4. pH Bajo (< 5.0)
        if (ph < rangosPh.min) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "pH Bajo (Acidez)",
                    valorActual = ph.toString(),
                    recomendacion = "pH < 5.0: Aplicar cal dolomítica o calcita.",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        // 5. Nitrógeno Bajo (< 60)
        if (nitrogeno < rangosNitrogeno.min) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "Nitrógeno Bajo (N)",
                    valorActual = "${String.format("%.1f", nitrogeno)}",
                    recomendacion = "Bajo N: Aplicar fertilizante nitrogenado.",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        // 6. Fósforo Bajo (< 20)
        if (fosforo < rangosFosforo.min) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "Fósforo Bajo (P)",
                    valorActual = "${String.format("%.1f", fosforo)}",
                    recomendacion = "Bajo P: Aplicar fuentes de fósforo (fosfato diamónico, roca fosfórica).",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        // 7. Potasio Bajo (< 60)
        if (potasio < rangosPotasio.min) {
            listaAlertas.add(
                AlertaInfo(
                    variable = "Potasio Bajo (K)",
                    valorActual = "${String.format("%.1f", potasio)}",
                    recomendacion = "Bajo K: Aplicar cloruro o sulfato de potasio.",
                    estado = EstadoValor.PELIGRO
                )
            )
        }

        return@remember listaAlertas
    }

    return alertas
}


@Composable
fun AlertasScreen(viewModel: SensorViewModel) {
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val status by viewModel.status.collectAsState()
    val latestData = sensorDataList.firstOrNull()

    // Obtenemos los colores de estado (Verde, Naranja, Rojo)
    val (textColor, strokeColor) = obtenerColoresEstado(status)

    // Obtenemos la lista de alertas filtradas
    val alertas = obtenerAlertas(latestData)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = com.practica.aplicacionedafoclimatica.R.drawable.wallpaper2),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.9f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Padding general
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text = "Alertas y Recomendaciones",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Estado de conexión
            Box(modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)) {
                Text(
                    text = status,
                    color = strokeColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                )
                Text(
                    text = status,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- Lista de Alertas ---
            if (alertas.isEmpty()) {
                // Mensaje si no hay alertas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Text(
                        text = "¡Todo en orden!\nNo hay alertas activas.",
                        color = Color(0xFF064e3b),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                // Lista si SÍ hay alertas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(alertas) { alerta ->
                        AlertaCard(alerta = alerta)
                    }
                }
            }
        }
    }
}

/**
 * Composable para mostrar una tarjeta de alerta individual.
 */
@Composable
fun AlertaCard(alerta: AlertaInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de Alerta
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Alerta",
                tint = alerta.estado.color, // Color dinámico (Rojo)
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            // Textos
            Column(modifier = Modifier.weight(1f)) {
                // Título de la Variable
                Text(
                    text = alerta.variable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF064e3b) // Verde oscuro
                )
                // Valor Actual
                Text(
                    text = "Valor: ${alerta.valorActual}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color(0xFF065f46) // Verde medio
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Recomendación
                Text(
                    text = alerta.recomendacion,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color(0xFF064e3b), // Verde oscuro
                    lineHeight = 20.sp
                )
            }
        }
    }
}