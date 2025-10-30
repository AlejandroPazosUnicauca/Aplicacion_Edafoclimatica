package com.practica.aplicacionedafoclimatica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import kotlin.Float

/*@Composable
fun CircleCard(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE6F4EA),
    iconColor: Color = Color(0xFF4CAF50),

) {
    Column(
        modifier = modifier
            .width(100.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            ),
            color = Color.Black
        )
    }
}*/

@Composable
fun CircleCard(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE6F4EA),
    iconColor: Color = Color(0xFF4CAF50),
    progress: Float = 1f, // 1f = 100%
    progressColor: Color = Color(0xFF4CAF50),
    progressBackground: Color = Color(0xFFE0E0E0)
) {
    Column(
        modifier = modifier
            .width(100.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(72.dp)
        ) {
            androidx.compose.material3.CircularProgressIndicator(
                progress = { progress },
                color = progressColor,
                trackColor = progressBackground,
                strokeWidth = 6.dp,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = iconColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            ),
            color = Color.Black
        )
    }
}
