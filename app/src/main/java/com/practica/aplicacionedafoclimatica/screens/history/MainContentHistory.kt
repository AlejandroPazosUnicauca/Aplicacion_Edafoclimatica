package com.practica.aplicacionedafoclimatica.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign


val fechas = listOf(
    "2023-05-01", "2023-05-02", "2023-05-03", "2023-05-04", "2023-05-05",
    "2023-06-01", "2023-06-02", "2023-06-03", "2023-06-04", "2023-06-05",
    "2024-01-10", "2024-01-11", "2024-01-12", "2024-01-13", "2024-01-14",
    "2024-02-20", "2024-02-21", "2024-02-22", "2024-02-23", "2024-02-24"
)

val datos = listOf(
    "Temperatura (°C)" to listOf(
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31"
    ),
    "Humedad (%)" to listOf(
        "60",
        "58",
        "55",
        "59",
        "57",
        "61",
        "62",
        "63",
        "64",
        "65",
        "66",
        "67",
        "68",
        "69",
        "70",
        "71",
        "72",
        "73",
        "74",
        "75"
    ),
    "pH" to listOf(
        "6.8",
        "7.0",
        "7.1",
        "6.9",
        "7.0",
        "7.2",
        "7.3",
        "7.1",
        "7.0",
        "6.8",
        "6.7",
        "6.9",
        "7.0",
        "7.1",
        "7.2",
        "7.3",
        "7.1",
        "7.0",
        "6.9",
        "6.8"
    ),
    "N" to listOf(
        "65",
        "63",
        "64",
        "66",
        "65",
        "67",
        "68",
        "69",
        "70",
        "71",
        "72",
        "73",
        "74",
        "75",
        "76",
        "77",
        "78",
        "79",
        "80",
        "81"
    ),
    "P" to listOf(
        "30",
        "32",
        "31",
        "29",
        "30",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47"
    ),
    "K" to listOf(
        "60",
        "62",
        "61",
        "59",
        "60",
        "63",
        "64",
        "65",
        "66",
        "67",
        "68",
        "69",
        "70",
        "71",
        "72",
        "73",
        "74",
        "75",
        "76",
        "77"
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentHistory(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = com.practica.aplicacionedafoclimatica.R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Calcular rango de años
        val anioMin = fechas.minOf { it.substring(0, 4).toInt() }
        val anioMax = fechas.maxOf { it.substring(0, 4).toInt() }
        val anios = (anioMin..anioMax).map { it.toString() }
        val meses = (1..12).map { it.toString().padStart(2, '0') }

        var anioSeleccionado by remember { mutableStateOf(anios.first()) }
        var mesSeleccionado by remember { mutableStateOf(meses.first()) }

        // Filtrar fechas y datos
        val fechasFiltradas = fechas.filter { it.startsWith("$anioSeleccionado-$mesSeleccionado") }
        val indicesFiltrados =
            fechas.mapIndexedNotNull { idx, f -> if (fechasFiltradas.contains(f)) idx else null }
        val datosFiltrados = datos.map { (param, valores) ->
            param to indicesFiltrados.map { valores[it] }
        }

        Column(modifier = modifier) {
            // Filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Filtro Año
                var expandedAnio by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedAnio,
                    onExpandedChange = { expandedAnio = !expandedAnio }
                ) {
                    TextField(
                        value = anioSeleccionado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Año") },
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
                        expanded = expandedAnio,
                        onDismissRequest = { expandedAnio = false }
                    ) {
                        anios.forEach { anio ->
                            DropdownMenuItem(
                                text = { Text(anio) },
                                onClick = {
                                    anioSeleccionado = anio
                                    expandedAnio = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                // Filtro Mes
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
            // Tabla con fechas completas y datos filtrados
            TablaAlertasConFechas(
                fechas = fechasFiltradas,
                datos = datosFiltrados,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun TablaAlertasConFechas(
    fechas: List<String>,
    datos: List<Pair<String, List<String>>>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
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
            // Encabezado
            Row(
                modifier = Modifier
                    .background(Color(0xFFd1fae5), RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Fecha",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.width(120.dp),
                    color = Color(0xFF059669),
                    textAlign = TextAlign.Center
                )
                fechas.forEach { fecha ->
                    Text(
                        text = fecha,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 12.dp),
                        color = Color(0xFF059669),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFd1fae5), thickness = 1.dp)
            // Filas de datos
            datos.forEachIndexed { index, (parametro, valores) ->
                Row(
                    modifier = Modifier
                        .background(
                            if (index % 2 == 0) Color.White else Color(0xFFf0fdf4),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = parametro,
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp,
                        modifier = Modifier.width(120.dp),
                        color = Color(0xFF059669),
                        textAlign = TextAlign.Center
                    )
                    valores.forEach { valor ->
                        Text(
                            text = valor,
                            fontSize = 17.sp,
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 12.dp),
                            color = Color(0xFF059669),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (index < datos.size - 1) {
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFFF5722))
                }
            }
        }
    }
}