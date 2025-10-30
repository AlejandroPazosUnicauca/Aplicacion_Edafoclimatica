package com.practica.aplicacionedafoclimatica.screens.alerts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.practica.aplicacionedafoclimatica.ui.components.MenuLateral

@Composable
fun AlertScreen() {
    Scaffold(
        //topBar = { TopBar(onMenuClick = {}) } // lambda vacía temporal
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // SideBar a la izquierda
            //SideBar(modifier = Modifier.width(250.dp))
            MenuLateral()

            // Área principal
            //MainContent(modifier = Modifier.fillMaxSize())   CAMBIAR
        }
    }
}