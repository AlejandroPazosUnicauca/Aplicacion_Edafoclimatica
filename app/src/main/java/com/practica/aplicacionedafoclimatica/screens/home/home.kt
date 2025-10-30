package com.practica.aplicacionedafoclimatica.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.practica.aplicacionedafoclimatica.ui.components.MenuLateral
import com.practica.aplicacionedafoclimatica.ui.components.SideBar
import com.practica.aplicacionedafoclimatica.ui.components.TopBar

@Composable
fun HomeScreen() {
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

