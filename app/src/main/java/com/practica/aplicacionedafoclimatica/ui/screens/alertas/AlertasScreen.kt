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
import com.practica.aplicacionedafoclimatica.domain.AlertaInfo
import com.practica.aplicacionedafoclimatica.ui.screens.medidas.obtenerColoresEstado
import com.practica.aplicacionedafoclimatica.ui.theme.color
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel

/**
 * Muestra la vista principal de alertas y recomendaciones activas del sistema.
 *
 * @param viewModel ViewModel que expone el estado de conexión y las alertas generadas.
 */
@Composable
fun AlertasScreen(viewModel: SensorViewModel) {
    val status by viewModel.status.collectAsState()
    val alertas by viewModel.alertasActivas.collectAsState()
    val (textColor, strokeColor) = obtenerColoresEstado(status)

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Alertas y Recomendaciones",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

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

            if (alertas.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Text(
                        text = "Todo en orden\nNo hay alertas activas.",
                        color = Color(0xFF064e3b),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
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
 * Representa una alerta individual con su valor actual y la recomendación asociada.
 *
 * @param alerta Datos de la alerta a visualizar.
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
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Alerta",
                tint = alerta.estado.color,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alerta.variable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF064e3b)
                )
                Text(
                    text = "Valor: ${alerta.valorActual}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color(0xFF065f46)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = alerta.recomendacion,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color(0xFF064e3b),
                    lineHeight = 20.sp
                )
            }
        }
    }
}