package com.practica.aplicacionedafoclimatica.ui.screens.registros

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practica.aplicacionedafoclimatica.R
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel
import com.practica.aplicacionedafoclimatica.data.model.SensorData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Muestra la pantalla de registros históricos del sensor con filtros por año y mes.
 *
 * @param modifier Modificador opcional para personalizar la composición.
 * @param viewModel ViewModel con los datos históricos del sensor.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrosScreen(
    modifier: Modifier = Modifier,
    viewModel: SensorViewModel
) {
    val context = LocalContext.current
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val status by viewModel.status.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = status,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (sensorDataList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sin datos recibidos aun",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                val fechas = sensorDataList.mapNotNull { it.fecha }
                val anos = if (fechas.isNotEmpty()) fechas.map { it.substring(0, 4) }.distinct().sorted() else listOf("2025")
                val meses = (1..12).map { it.toString().padStart(2, '0') }

                var anoSeleccionado by remember { mutableStateOf(anos.last()) }
                var mesSeleccionado by remember { mutableStateOf(meses.first()) }

                val datosFiltrados = sensorDataList.filter {
                    it.fecha.startsWith("$anoSeleccionado-$mesSeleccionado")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    var expandedAno by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedAno,
                        onExpandedChange = { expandedAno = !expandedAno }
                    ) {
                        TextField(
                            value = anoSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ano") },
                            modifier = Modifier
                                .menuAnchor()
                                .width(120.dp)
                                .height(56.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF059669),
                                unfocusedTextColor = Color(0xFF059669),
                                focusedContainerColor = Color(0xFFd1fae5),
                                unfocusedContainerColor = Color(0xFFf0fdf4),
                                focusedIndicatorColor = Color(0xFF059669),
                                unfocusedIndicatorColor = Color(0xFF34d399),
                                focusedLabelColor = Color(0xFF059669),
                                unfocusedLabelColor = Color(0xFF059669)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedAno,
                            onDismissRequest = { expandedAno = false }
                        ) {
                            anos.forEach { ano ->
                                DropdownMenuItem(
                                    text = { Text(ano) },
                                    onClick = {
                                        anoSeleccionado = ano
                                        expandedAno = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    var expandedMes by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedMes,
                        onExpandedChange = { expandedMes = !expandedMes }
                    ) {
                        TextField(
                            value = mesSeleccionado,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Mes") },
                            modifier = Modifier
                                .menuAnchor()
                                .width(120.dp)
                                .height(56.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF059669),
                                unfocusedTextColor = Color(0xFF059669),
                                focusedContainerColor = Color(0xFFd1fae5),
                                unfocusedContainerColor = Color(0xFFf0fdf4),
                                focusedIndicatorColor = Color(0xFF059669),
                                unfocusedIndicatorColor = Color(0xFF34d399),
                                focusedLabelColor = Color(0xFF059669),
                                unfocusedLabelColor = Color(0xFF059669)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMes,
                            onDismissRequest = { expandedMes = false }
                        ) {
                            meses.forEach { mes ->
                                DropdownMenuItem(
                                    text = { Text(mes) },
                                    onClick = {
                                        mesSeleccionado = mes
                                        expandedMes = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                TablaDatos(sensorDataList = datosFiltrados)
            }
        }
    }
}

/**
 * Renderiza una tabla con los registros históricos del sensor para una fecha concreta.
 *
 * @param sensorDataList Lista de lecturas a representar en la tabla.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TablaDatos(sensorDataList: List<SensorData>) {
    if (sensorDataList.isEmpty()) {
        Text(
            text = "No hay registros para este mes.",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        return
    }

    val scrollState = rememberScrollState()
    val colorTextoTabla = Color(0xFF064e3b)

    val camposOrdenados = listOf(
        "Lumenes",
        "Temperatura_ambiente",
        "Humedad_ambiente",
        "Lluvia",
        "Humedad_suelo",
        "Temperatura_suelo",
        "Ph",
        "Fosforo",
        "Nitrogeno",
        "Potasio"
    )

    val listaInvertida = sensorDataList.reversed()
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy\nh:mm a", Locale.getDefault())

    val fechasHeaders = listaInvertida.map { data ->
        try {
            LocalDateTime.parse(data.fecha, DateTimeFormatter.ISO_DATE_TIME)
                .format(dateFormatter)
        } catch (e: Exception) {
            data.fecha
        }
    }

    val alturaDeFila = 70.dp
    val alturaHeader = 65.dp

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .background(Color(0xFFf0fdf4), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.width(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFFd1fae5), RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .height(alturaHeader),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Variable",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorTextoTabla,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFd1fae5), thickness = 1.dp)

            camposOrdenados.forEachIndexed { index, campo ->
                Row(
                    modifier = Modifier
                        .background(
                            if (index % 2 == 0) Color.White else Color(0xFFf0fdf4),
                            RoundedCornerShape(6.dp)
                        )
                        .fillMaxWidth()
                        .height(alturaDeFila),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = campo.replace("_", " ").replace("N", "N").replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        color = colorTextoTabla,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                if (index < camposOrdenados.size - 1) {
                    Divider(thickness = 0.5.dp, color = Color(0xFFd1fae5))
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFFd1fae5), RoundedCornerShape(8.dp))
                    .height(alturaHeader),
                verticalAlignment = Alignment.CenterVertically
            ) {
                fechasHeaders.forEach { fecha ->
                    Text(
                        text = fecha,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.width(120.dp),
                        color = colorTextoTabla,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFd1fae5), thickness = 1.dp)

            camposOrdenados.forEachIndexed { index, campo ->
                val valores = listaInvertida.map { data ->
                    val field = data::class.java.getDeclaredField(campo)
                    field.isAccessible = true
                    field.get(data)?.toString() ?: "-"
                }

                Row(
                    modifier = Modifier
                        .background(
                            if (index % 2 == 0) Color.White else Color(0xFFf0fdf4),
                            RoundedCornerShape(6.dp)
                        )
                        .height(alturaDeFila),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Solo mostramos los valores
                    valores.forEach { valor ->
                        Text(
                            text = valor,
                            fontSize = 17.sp,
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 8.dp),
                            color = colorTextoTabla, // <-- Color oscuro
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (index < camposOrdenados.size - 1) {
                    Divider(thickness = 0.5.dp, color = Color(0xFFd1fae5))
                }
            }
        } // Fin de la Columna Desplazable
    }
}