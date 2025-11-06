package com.practica.aplicacionedafoclimatica.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Clase sellada que define las pantallas principales de la aplicación.
 * Cada objeto representa una ruta de navegación dentro del Drawer.
 */
sealed class AppScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Medidas : AppScreen(
        route = "medidas",
        title = "Medidas",
        icon = Icons.Default.Assessment
    )

    object Registros : AppScreen(
        route = "registros",
        title = "Registros",
        icon = Icons.Default.List
    )

    object Alertas : AppScreen(
        route = "alertas",
        title = "Alertas",
        icon = Icons.Default.Notifications
    )
}