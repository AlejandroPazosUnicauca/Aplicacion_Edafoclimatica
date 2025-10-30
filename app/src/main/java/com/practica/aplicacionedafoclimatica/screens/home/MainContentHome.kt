package com.practica.aplicacionedafoclimatica.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var temperature by remember { mutableStateOf(25) }
    var humidity by remember { mutableStateOf(50) }
    var ph by remember { mutableStateOf(7f) }
    var nitrogen by remember { mutableStateOf(65) }
    var phosphorus by remember { mutableStateOf(30) }
    var potassium by remember { mutableStateOf(60) }
    var measuring by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = com.practica.aplicacionedafoclimatica.R.drawable.wallpaper),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
          alpha = 0.9f
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mediciones Actuales",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669)
            )
            // Primera fila: Temperatura, Humedad, pH
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                /*CircularVariable(
                    label = "Temperatura (°C)",
                    value = temperature,
                    max = 50,
                    modifier = Modifier.weight(1f),
                    onClick = { if (measuring) temperature = (0..50).random() }
                )
                CircularVariable(
                    label = "Humedad (%)",
                    value = humidity,
                    max = 100,
                    modifier = Modifier.weight(1f),
                    onClick = { if (measuring) humidity = (0..100).random() }
                )*/
                NutrientCircle("Temperatura (°C)", temperature, modifier = Modifier.weight(1f))
                NutrientCircle("Humedad (%)", humidity, modifier = Modifier.weight(1f))
                PhBar(
                    label = "pH",
                    value = ph,
                    modifier = Modifier.weight(1f)
                )
            }

            // Segunda fila: N, P, K
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NutrientCircle("Nitrógeno (N)", nitrogen, modifier = Modifier.weight(1f))
                NutrientCircle("Fósforo (P)", phosphorus, modifier = Modifier.weight(1f))
                NutrientCircle("Potasio (K)", potassium, modifier = Modifier.weight(1f))
            }

            // Botón de reiniciar medida
            Button(
                onClick = {
                    if (measuring) {
                        measuring = false
                    } else {
                        temperature = 25
                        humidity = 50
                        ph = 7f
                        nitrogen = 65
                        phosphorus = 30
                        potassium = 60
                        measuring = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (measuring) Color.Red else Color(0xFF059669),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(48.dp)
                    .width(200.dp)
            ) {
                Text(if (measuring) "Reiniciar medida" else "Medición completada")
            }
        }
    }
}

@Composable
fun CircularVariable(
    label: String,
    value: Int,
    max: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(label, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFd1fae5), shape = CircleShape)
        ) {
            Text(
                value.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF059669)
            )
        }
    }
}

/*@Composable
fun CircularVariable(
    label: String,
    value: Int,
    max: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val progress = value.toFloat() / max.toFloat()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(label, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            CircularProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                color = Color(0xFF059669),
                trackColor = Color(0xFFd1fae5),
                strokeWidth = 8.dp,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFd1fae5), shape = CircleShape)
            ) {
                Text(
                    value.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF059669)
                )
            }
        }
    }
}*/

@Composable
fun PhBar(
    label: String,
    value: Float,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(label, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Color(0xFFd1fae5), shape = RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = value / 14f)
                    .background(Color(0xFF059669), shape = RoundedCornerShape(12.dp))
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF059669)
        )
    }
}

@Composable
fun NutrientCircle(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(label, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFd1fae5), shape = CircleShape)
        ) {
            Text(
                value.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF059669)
            )
        }
    }
}
