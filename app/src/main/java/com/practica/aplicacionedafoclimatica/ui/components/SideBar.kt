package com.practica.aplicacionedafoclimatica.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SideBar() {
    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(0.dp)
            )
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SidebarButton(
            icon = Icons.AutoMirrored.Filled.List,
            label = "Registros",
            onClick = { /* Acción de registros */ }
        )
        SidebarButton(
            icon = Icons.Default.Notifications,
            label = "Alertas",
            onClick = { /* Acción de alertas */ }
        )
        SidebarButton(
            icon = Icons.Default.Calculate,
            label = "Calcular abonada",
            onClick = { /* Acción de calcular */ }
        )
    }
}

@Composable
fun SidebarButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF059669) // verde similar al mockup
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color(0xFF059669)
        )
    }
}