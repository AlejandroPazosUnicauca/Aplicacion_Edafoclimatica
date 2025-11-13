package com.practica.aplicacionedafoclimatica

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practica.aplicacionedafoclimatica.ui.navigation.AppNavHost
import com.practica.aplicacionedafoclimatica.ui.screens.DrawerMenu
import com.practica.aplicacionedafoclimatica.ui.screens.config.ConfigScreen
import com.practica.aplicacionedafoclimatica.ui.theme.AplicacionEdafoclimaticaTheme
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicacionEdafoclimaticaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Crear el NavController principal
                    val navController = rememberNavController()

                    // 2. Crear UNA instancia del ViewModel
                    //    Se compartirá entre ConfigScreen y DrawerMenu
                    val sensorViewModel: SensorViewModel = viewModel()

                    // 3. Definir el NavHost
                    DrawerMenu(viewModel= sensorViewModel)
                }

            }
        }
    }
}