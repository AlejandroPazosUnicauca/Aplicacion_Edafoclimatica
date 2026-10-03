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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.practica.aplicacionedafoclimatica.domain.EstadoValor
import com.practica.aplicacionedafoclimatica.domain.RangosAlerta
import com.practica.aplicacionedafoclimatica.domain.obtenerEstadoValor
import com.practica.aplicacionedafoclimatica.domain.rangosFosforo
import com.practica.aplicacionedafoclimatica.domain.rangosHumedadAmbiente
import com.practica.aplicacionedafoclimatica.domain.rangosHumedadSuelo
import com.practica.aplicacionedafoclimatica.domain.rangosLluvia
import com.practica.aplicacionedafoclimatica.domain.rangosLumenes
import com.practica.aplicacionedafoclimatica.domain.rangosNitrogeno
import com.practica.aplicacionedafoclimatica.domain.rangosPh
import com.practica.aplicacionedafoclimatica.domain.rangosPotasio
import com.practica.aplicacionedafoclimatica.domain.rangosTempAmbiente
import com.practica.aplicacionedafoclimatica.domain.rangosTempSuelo
import com.practica.aplicacionedafoclimatica.ui.theme.color
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel

data class VariableMedida(
    val label: String,
    val valor: Float,
    val rangos: RangosAlerta,
    val estado: EstadoValor
)

/**
 * Devuelve los colores asociados al estado actual de la conexión o del sistema.
 *
 * @param status Texto del estado actual de la conexión.
 * @return Pareja de colores con el texto y el contorno que corresponden al estado.
 */
@Composable
fun obtenerColoresEstado(status: String): Pair<Color, Color> {
    return remember(status) {
        when {
            status.contains("Error", ignoreCase = true) ||
                    status.contains("Desconectado", ignoreCase = true) -> {
                Color(0xFFDC2626) to Color.White
            }
            status.contains("Conectando", ignoreCase = true) ||
                    status.contains("Esperando", ignoreCase = true) -> {
                Color(0xFFF59E0B) to Color.Black
            }
            else -> {
                Color(0xFF10B981) to Color.Black
            }
        }
    }
}

/**
 * Muestra la pantalla con las mediciones actuales del sensor en formato visual por variables.
 *
 * @param navController Controlador de navegación de la pantalla actual.
 * @param viewModel ViewModel con los datos más recientes del sensor.
 */
@Composable
fun MedidasScreen(navController: NavHostController, viewModel: SensorViewModel) {
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val status by viewModel.status.collectAsState()

    val latestData = sensorDataList.firstOrNull()
    val (textColor, strokeColor) = obtenerColoresEstado(status)

    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    val lumenes = latestData?.Lumenes?.toFloat() ?: 0f
    val temperaturaAmbiente = latestData?.Temperatura_ambiente?.toFloat() ?: 0f
    val humedadAmbiente = latestData?.Humedad_ambiente?.toFloat() ?: 0f
    val lluvia = latestData?.Lluvia?.toFloat() ?: 0f
    val humedadSuelo = latestData?.Humedad_suelo?.toFloat() ?: 0f
    val temperaturaSuelo = latestData?.Temperatura_suelo?.toFloat() ?: 0f
    val ph = latestData?.Ph ?: 0f
    val fosforo = latestData?.Fosforo?.toFloat() ?: 0f
    val nitrogeno = latestData?.Nitrogeno?.toFloat() ?: 0f
    val potasio = latestData?.Potasio?.toFloat() ?: 0f

    val variables = listOf(
        VariableMedida("Lúmenes (Lux)", lumenes, rangosLumenes, obtenerEstadoValor(lumenes, rangosLumenes)),
        VariableMedida("Temp. ambiente (°C)", temperaturaAmbiente, rangosTempAmbiente, obtenerEstadoValor(temperaturaAmbiente, rangosTempAmbiente)),
        VariableMedida("Humedad ambiente (%)", humedadAmbiente, rangosHumedadAmbiente, obtenerEstadoValor(humedadAmbiente, rangosHumedadAmbiente)),
        VariableMedida("Lluvia (mm)", lluvia, rangosLluvia, obtenerEstadoValor(lluvia, rangosLluvia)),
        VariableMedida("Humedad del suelo (%)", humedadSuelo, rangosHumedadSuelo, obtenerEstadoValor(humedadSuelo, rangosHumedadSuelo)),
        VariableMedida("Temp. del suelo (°C)", temperaturaSuelo, rangosTempSuelo, obtenerEstadoValor(temperaturaSuelo, rangosTempSuelo)),
        VariableMedida("Fósforo (P)", fosforo, rangosFosforo, obtenerEstadoValor(fosforo, rangosFosforo)),
        VariableMedida("Nitrógeno (N)", nitrogeno, rangosNitrogeno, obtenerEstadoValor(nitrogeno, rangosNitrogeno)),
        VariableMedida("Potasio (K)", potasio, rangosPotasio, obtenerEstadoValor(potasio, rangosPotasio))
    )

    val estadoPh = obtenerEstadoValor(ph, rangosPh)

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mediciones Actuales",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Box(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = status,
                    color = strokeColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(
                        x = 1.dp,
                        y = 1.dp
                    )
                )
                Text(
                    text = status,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            for (i in variables.indices step 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val item1 = variables[i]
                    VariableCircle(
                        label = item1.label,
                        value = item1.valor,
                        maxValue = item1.rangos.maxAbsoluto,
                        status = item1.estado,
                        modifier = Modifier.weight(1f)
                    )

                    if (i + 1 < variables.size) {
                        val item2 = variables[i + 1]
                        VariableCircle(
                            label = item2.label,
                            value = item2.valor,
                            maxValue = item2.rangos.maxAbsoluto,
                            status = item2.estado,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            PhBar(
                label = "Nivel de pH",
                value = ph,
                status = estadoPh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

/**
 * Dibuja un círculo con progreso para representar una variable y su estado de salud.
 *
 * @param label Nombre de la variable mostrada.
 * @param value Valor actual de la medida.
 * @param maxValue Máximo absoluto usado para escalar el progreso.
 * @param status Estado que determina el color del indicador.
 * @param modifier Modificador de composición opcional.
 */
@Composable
fun VariableCircle(
    label: String,
    value: Float,
    maxValue: Float,
    status: EstadoValor,
    modifier: Modifier = Modifier
) {
    val progress = (value / maxValue).coerceIn(0f, 1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(190.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .padding(12.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF065F46),
            maxLines = 2,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(110.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawArc(
                    color = Color(0xFFd1fae5),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = status.color,
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

/**
 * Muestra una barra horizontal de progreso para representar el nivel de pH actual.
 *
 * @param label Nombre visible de la métrica.
 * @param value Valor de pH actual.
 * @param status Estado que decide el color de la barra.
 * @param modifier Modificador de composición opcional.
 */
@Composable
fun PhBar(
    label: String,
    value: Float,
    status: EstadoValor,
    modifier: Modifier = Modifier
) {
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
                    .background(
                        color = status.color,
                        shape = RoundedCornerShape(12.dp)
                    )
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