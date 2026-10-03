package com.practica.aplicacionedafoclimatica.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practica.aplicacionedafoclimatica.ui.screens.medidas.MedidasScreen
import com.practica.aplicacionedafoclimatica.ui.screens.alertas.AlertasScreen
import com.practica.aplicacionedafoclimatica.ui.screens.registros.RegistrosScreen
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel
import com.practica.aplicacionedafoclimatica.ui.screens.config.ConfigScreen

/**
 * Configura el grafo de navegación principal de la aplicación según el estado de configuración del sensor.
 *
 * @param navController Controlador de navegación que gestiona las pantallas.
 * @param viewModel ViewModel central con el estado de conexión y lecturas del sensor.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, viewModel: SensorViewModel) {
    val isConfigured by viewModel.isConfigured.collectAsState(initial = false)

    val startRoute = if (isConfigured) {
        AppScreen.Medidas.route
    } else {
        AppScreen.Configuracion.route
    }

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable(AppScreen.Configuracion.route) {
            ConfigScreen(
                viewModel = viewModel,
                onConnectSuccess = {
                    navController.navigate(AppScreen.Medidas.route) {
                        popUpTo(AppScreen.Configuracion.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppScreen.Medidas.route) {
            MedidasScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppScreen.Registros.route) {
            RegistrosScreen(viewModel = viewModel)
        }
        composable(AppScreen.Alertas.route) {
            AlertasScreen(viewModel = viewModel)
        }
    }
}