package com.practica.aplicacionedafoclimatica.ui.screens.medidas

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel
import java.time.format.TextStyle


@Composable
fun MedidasScreen(viewModel: SensorViewModel = viewModel()) {
    // Se obtienen los datos del ViewModel
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val status by viewModel.status.collectAsState()

    // Último registro recibido
    val latestData = sensorDataList.lastOrNull()

    // Iniciar la conexión automáticamente
    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    val lumenes = latestData?.Lumenes ?: 0.0
    val temperaturaAmbiente = latestData?.Temperatura_ambiente ?: 0.0
    val humedadAmbiente = latestData?.Humedad_ambiente ?: 0.0
    val lluvia = latestData?.Lluvia ?: 0.0
    val humedadSuelo = latestData?.Humedad_suelo ?: 0.0
    val temperaturaSuelo = latestData?.Temperatura_suelo ?: 0.0
    val ph = latestData?.Ph ?: 0f
    val fosforo = latestData?.Fosforo ?: 0.0
    val nitrogeno = latestData?.Nitrogeno ?: 0.0
    val potasio = latestData?.Potasio ?: 0.0

    val variables = listOf(
        Triple("Lúmenes (lm)", lumenes.toFloat(), 1000f),
        Triple("Temperatura ambiente (°C)", temperaturaAmbiente.toFloat(), 50f),
        Triple("Humedad ambiente (%)", humedadAmbiente.toFloat(), 100f),
        Triple("Lluvia (mm)", lluvia.toFloat(), 100f),
        Triple("Humedad del suelo (%)", humedadSuelo.toFloat(), 100f),
        Triple("Temperatura del suelo (°C)", temperaturaSuelo.toFloat(), 50f),
        Triple("Fósforo (P)", fosforo.toFloat(), 100f),
        Triple("Nitrógeno (N)", nitrogeno.toFloat(), 100f),
        Triple("Potasio (K)", potasio.toFloat(), 100f)
    )

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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            //verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título
            Text(
                text = "Mediciones Actuales",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            // Estado de conexión
            Box {
                Text(
                    text = status,
                    color = Color.Black, // Contorno
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.offset(x = 1.dp, y = 1.dp)
                )
                Text(
                    text = status,
                    color = Color.White, // Texto principal
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            for (i in variables.indices step 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    VariableCircle(
                        label = variables[i].first,
                        value = variables[i].second,
                        maxValue = variables[i].third,
                        modifier = Modifier.weight(1f)
                    )

                    if (i + 1 < variables.size) {
                        VariableCircle(
                            label = variables[i + 1].first,
                            value = variables[i + 1].second,
                            maxValue = variables[i + 1].third,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // pH con barra horizontal
            Spacer(modifier = Modifier.height(24.dp))
            PhBar(
                label = "Nivel de pH",
                value = ph,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de actualización
            Button(
                onClick = { /*viewModel.actualizarValoresAleatorios() */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(50.dp)
                    .width(220.dp)
            ) {
                Text("Actualizar medición", color = Color.White)
            }
        }
    }
}

@Composable
fun VariableCircle(
    label: String,
    value: Float,
    maxValue: Float,
    modifier: Modifier = Modifier
) {
    val progress = (value / maxValue).coerceIn(0f, 1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(190.dp) // 👈 Tamaño fijo para todos los cuadros
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .padding(12.dp)
    ) {
        // 🔹 Título centrado con límite de líneas
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF065F46),
            maxLines = 2, // evita que crezca más de 2 líneas
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // 🔹 Separador visual
        Spacer(modifier = Modifier.height(6.dp))

        // 🔹 Indicador circular de valor
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(110.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                // Fondo circular
                drawArc(
                    color = Color(0xFFd1fae5),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
                // Progreso del valor
                drawArc(
                    color = Color(0xFF059669),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Text(
                text = String.format("%.1f", value),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF065F46)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun PhBar(label: String, value: Float, modifier: Modifier = Modifier) {
    val normalized = (value / 14f).coerceIn(0f, 1f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF065F46))
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp)
                .background(Color(0xFFd1fae5), shape = RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(normalized)
                    .background(Color(0xFF059669), shape = RoundedCornerShape(12.dp))
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF065F46)
        )
    }
}