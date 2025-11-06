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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrosScreen(
    modifier: Modifier = Modifier,
    viewModel: SensorViewModel = viewModel()
) {
    val context = LocalContext.current
    val sensorDataList by viewModel.sensorDataList.collectAsState()
    val status by viewModel.status.collectAsState()

    // 🟢 Conectar automáticamente al entrar en la pantalla
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

            // --- Estado de conexión ---
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
                val anos = fechas.map { it.substring(0, 4) }.distinct().sorted()
                val meses = (1..12).map { it.toString().padStart(2, '0') }

                var anoSeleccionado by remember { mutableStateOf(anos.last()) }
                var mesSeleccionado by remember { mutableStateOf(meses.first()) }

                val datosFiltrados = sensorDataList.filter {
                    it.fecha.startsWith("$anoSeleccionado-$mesSeleccionado")
                }

                // --- Filtros ---
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TablaDatos(sensorDataList: List<SensorData>) {
    if (sensorDataList.isEmpty()) {
        Text(
            text = "No hay registros para este mes.",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        return
    }

    val scrollState = rememberScrollState()

    // 🟢 CLAVE: Definir el orden exacto de las variables aqui
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

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(Color(0xFFf0fdf4), RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Encabezado ---
            Row(
                modifier = Modifier
                    .background(Color(0xFFd1fae5), RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Variable",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.width(150.dp),
                    color = Color(0xFF059669),
                    textAlign = TextAlign.Center
                )
                sensorDataList.forEach { data ->
                    val fecha = try {
                        LocalDateTime.parse(data.fecha, DateTimeFormatter.ISO_DATE_TIME)
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } catch (e: Exception) {
                        data.fecha
                    }
                    Text(
                        text = fecha,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.width(120.dp),
                        color = Color(0xFF059669),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFd1fae5), thickness = 1.dp)

            // --- Filas dinámicas ---
            // 💡 Ahora iteramos sobre la lista de campos ordenada
            camposOrdenados.forEachIndexed { index, campo ->
                val valores = sensorDataList.map { data ->
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
                        .padding(vertical = 10.dp, horizontal = 8.dp)
                ) {
                    // Reemplazamos la ñ en el nombre de la variable para evitar problemas
                    Text(
                        text = campo.replace("_", " ").replace("N", "N").replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        modifier = Modifier.width(150.dp),
                        color = Color(0xFF059669),
                        textAlign = TextAlign.Center
                    )
                    valores.forEach { valor ->
                        Text(
                            text = valor,
                            fontSize = 17.sp,
                            modifier = Modifier.width(120.dp),
                            color = Color(0xFF059669),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (index < camposOrdenados.size - 1) {
                    // Cambie el color del divisor de nuevo a uno que contraste bien (o usa el que tenias originalmente)
                    Divider(thickness = 0.5.dp, color = Color(0xFFd1fae5))
                }
            }
        }
    }
}