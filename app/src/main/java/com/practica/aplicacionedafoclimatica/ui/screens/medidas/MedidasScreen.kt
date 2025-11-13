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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel
import java.time.format.TextStyle


/**
 * Define el estado de un valor para el código de colores.
 */
enum class EstadoValor(val color: Color) {
    OPTIMO(Color(0xFF059669)),     // Verde
    ADVERTENCIA(Color(0xFFF59E0B)), // Amarillo/Ámbar
    PELIGRO(Color(0xFFDC2626))      // Rojo
}

/**
 * Almacena los rangos para una variable específica.
 * optimoStart y optimoEnd definen el rango "Verde".
 * min y max definen el rango "Amarillo".
 * Por debajo de min o por encima de max es "Rojo".
 */
data class RangosAlerta(
    val min: Float,
    val optimoStart: Float,
    val optimoEnd: Float,
    val max: Float,
    val maxAbsoluto: Float // El valor máximo para el medidor (ej. 100% o 25000 Lux)
)

/**
 * Contenedor para la información de cada medidor.
 */
data class VariableMedida(
    val label: String,
    val valor: Float,
    val rangos: RangosAlerta,
    val estado: EstadoValor
)

/**
 * Colores para el estado de la conexión (sin cambios).
 */
@Composable
fun obtenerColoresEstado(status: String): Pair<Color, Color> {
    return remember(status) {
        when {
            status.contains("Error", ignoreCase = true) ||
                    status.contains("Desconectado", ignoreCase = true) -> {
                Color(0xFFDC2626) to Color.White // Rojo
            }
            status.contains("Conectando", ignoreCase = true) ||
                    status.contains("Esperando", ignoreCase = true) -> {
                Color(0xFFF59E0B) to Color.Black // Naranja
            }
            else -> {
                Color(0xFF10B981) to Color.Black // Verde
            }
        }
    }
}

/**
 * Calcula el estado (color) de un valor según sus rangos.
 */
fun obtenerEstadoValor(valor: Float, rangos: RangosAlerta): EstadoValor {
    return when {
        // Óptimo (Verde)
        valor >= rangos.optimoStart && valor <= rangos.optimoEnd -> EstadoValor.OPTIMO
        // Advertencia (Amarillo)
        (valor >= rangos.min && valor < rangos.optimoStart) ||
                (valor > rangos.optimoEnd && valor <= rangos.max) -> EstadoValor.ADVERTENCIA
        // Peligro (Rojo)
        else -> EstadoValor.PELIGRO
    }
}

val rangosLumenes = RangosAlerta(min = 7000f, optimoStart = 12000f, optimoEnd = 20000f, max = 25000f, maxAbsoluto = 25000f)
val rangosTempAmbiente = RangosAlerta(min = 17f, optimoStart = 19f, optimoEnd = 21.5f, max = 23f, maxAbsoluto = 50f) // Max absoluto 50 (arbitrario)
val rangosHumedadAmbiente = RangosAlerta(min = 60f, optimoStart = 75f, optimoEnd = 85f, max = 90f, maxAbsoluto = 100f)
val rangosLluvia = RangosAlerta(min = 1000f, optimoStart = 1660f, optimoEnd = 2000f, max = 3000f, maxAbsoluto = 4000f)
val rangosHumedadSuelo = RangosAlerta(min = 40f, optimoStart = 75f, optimoEnd = 85f, max = 90f, maxAbsoluto = 100f)
val rangosTempSuelo = RangosAlerta(min = 17f, optimoStart = 19f, optimoEnd = 22f, max = 27f, maxAbsoluto = 35f) // Max absoluto 50
val rangosFosforo = RangosAlerta(min = 20f, optimoStart = 20f, optimoEnd = 30f, max = 40f, maxAbsoluto = 50f) // Max absoluto 100
val rangosNitrogeno = RangosAlerta(min = 60f, optimoStart = 60f, optimoEnd = 80f, max = 100f, maxAbsoluto = 120f)
val rangosPotasio = RangosAlerta(min = 60f, optimoStart = 60f, optimoEnd = 80f, max = 100f, maxAbsoluto = 120f)
val rangosPh = RangosAlerta(min = 5.0f, optimoStart = 5.5f, optimoEnd = 6.5f, max = 7.5f, maxAbsoluto = 14f) // Max absoluto 14


@Composable
fun MedidasScreen(navController: NavHostController, viewModel: SensorViewModel) {
    // Se obtienen los datos del ViewModel
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val status by viewModel.status.collectAsState()

    // Primer registro recibido
    val latestData = sensorDataList.firstOrNull()
    val (textColor, strokeColor) = obtenerColoresEstado(status)

    // Iniciar la conexión automáticamente
    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    // Obtenemos los valores o usamos 0 por defecto
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

    // Calculamos el estado del pH por separado
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
            Box(modifier = Modifier.padding(top = 8.dp)) {
                // Contorno (Stroke)
                Text(
                    text = status,
                    color = strokeColor, // Usamos el color de contorno dinámico
                    fontSize = 18.sp, // Un poco más grande para más impacto
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(
                        x = 1.dp,
                        y = 1.dp
                    ) // Offset para crear el efecto de contorno
                )
                // Texto principal
                Text(
                    text = status,
                    color = textColor, // Usamos el color principal dinámico (Rojo, Verde o Naranja)
                    fontSize = 18.sp, // Debe ser del mismo tamaño que el contorno
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
                        status = item1.estado, // 👈 Pasamos el estado (color)
                        modifier = Modifier.weight(1f)
                    )

                    if (i + 1 < variables.size) {
                        val item2 = variables[i+1]
                        VariableCircle(
                            label = item2.label,
                            value = item2.valor,
                            maxValue = item2.rangos.maxAbsoluto,
                            status = item2.estado, // 👈 Pasamos el estado (color)
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
                status = estadoPh, // 👈 Pasamos el estado (color)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun VariableCircle(
    label: String,
    value: Float,
    maxValue: Float,
    status: EstadoValor, // 👈 Acepta el estado
    modifier: Modifier = Modifier
) {
    // El progreso se calcula sobre el máximo absoluto (ej. 100% o 25000 Lux)
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
                    color = status.color, // 👈 Usamos el color del estado
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
fun PhBar(
    label: String,
    value: Float,
    status: EstadoValor, // 👈 Acepta el estado
    modifier: Modifier = Modifier
) {
    // El progreso se sigue calculando sobre 14
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
                        color = status.color, // 👈 Usamos el color del estado
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