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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, viewModel: SensorViewModel) {

    // Obtenemos el estado de configuración desde el ViewModel.
    val isConfigured by viewModel.isConfigured.collectAsState(initial = false)

    // El punto de inicio es dinámico: Medidas si está configurado, Configuracion si no.
    val startRoute = if (isConfigured) {
        AppScreen.Medidas.route
    } else {
        AppScreen.Configuracion.route
    }

    NavHost(
        navController = navController,
        startDestination = startRoute // Usamos la ruta inicial determinada dinámicamente
    ) {
        // RUTA DE CONFIGURACIÓN
        composable(AppScreen.Configuracion.route) {
            // **IMPORTANTE: Pasamos la lógica de navegación como un callback.**
            ConfigScreen(
                viewModel = viewModel,
                onConnectSuccess = {
                    // La navegación ocurre aquí, dentro del contexto del NavHost.
                    navController.navigate(AppScreen.Medidas.route) {
                        // Limpia la pila para que la configuración ya no sea accesible
                        popUpTo(AppScreen.Configuracion.route) { inclusive = true }
                    }
                }
            )
        }

        // RUTAS PRINCIPALES DEL MENÚ LATERAL
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