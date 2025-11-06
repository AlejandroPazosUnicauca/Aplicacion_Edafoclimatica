package com.practica.aplicacionedafoclimatica.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practica.aplicacionedafoclimatica.ui.screens.medidas.MedidasScreen
import com.practica.aplicacionedafoclimatica.ui.screens.alertas.AlertasScreen
import com.practica.aplicacionedafoclimatica.ui.screens.registros.RegistrosScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Medidas.route
    ) {
        composable(AppScreen.Medidas.route) { MedidasScreen() }
        composable(AppScreen.Registros.route) { RegistrosScreen() }
        composable(AppScreen.Alertas.route) { AlertasScreen() }
    }
}