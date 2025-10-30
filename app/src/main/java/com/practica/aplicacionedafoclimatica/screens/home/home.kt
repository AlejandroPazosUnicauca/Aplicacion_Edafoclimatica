package com.practica.aplicacionedafoclimatica.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo con imagen
        Image(
            painter = rememberAsyncImagePainter("https://storage.googleapis.com/a1aa/image/6a52b381-cea7-4c65-0f7c-ac16364fa4e4.jpg"),
            contentDescription = "Fondo cafetal",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            Row(modifier = Modifier.weight(1f)) {
                Sidebar(modifier = Modifier.width(180.dp))
                MainContent(modifier = Modifier.weight(1f))
            }
        }
    }
}