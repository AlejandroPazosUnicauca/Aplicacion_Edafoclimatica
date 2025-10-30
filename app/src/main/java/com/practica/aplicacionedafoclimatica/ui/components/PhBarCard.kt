package com.practica.aplicacionedafoclimatica.ui.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PhBarCard(
    modifier: Modifier = Modifier,
    phValue: Float // Se espera un valor entre 0 y 14
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "pH: ${String.format("%.1f", phValue)}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            // Barra de pH con gradiente de colores
            val barWidth = size.width
            val barHeight = size.height

            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFEF5350), // ácido - rojo
                        Color(0xFFFFC107), // neutro - amarillo
                        Color(0xFF66BB6A)  // alcalino - verde
                    )
                ),
                size = Size(barWidth, barHeight)
            )

            // Marcador del valor actual de pH
            val position = (phValue.coerceIn(0f, 14f) / 14f) * barWidth
            drawLine(
                color = Color.Black,
                start = Offset(position, 0f),
                end = Offset(position, barHeight),
                strokeWidth = 4f
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "0", fontSize = 12.sp)
            Text(text = "7", fontSize = 12.sp)
            Text(text = "14", fontSize = 12.sp)
        }
    }
}
