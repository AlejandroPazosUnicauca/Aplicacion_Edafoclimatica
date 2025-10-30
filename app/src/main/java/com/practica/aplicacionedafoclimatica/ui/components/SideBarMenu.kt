package com.practica.aplicacionedafoclimatica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practica.aplicacionedafoclimatica.screens.home.MainContentHome
import com.practica.aplicacionedafoclimatica.screens.history.MainContentHistory
import com.practica.aplicacionedafoclimatica.screens.alerts.MainContentAlert
import kotlinx.coroutines.launch

@Composable
fun MenuLateral() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                //containerColor = Color(0xFFf0fdf4)
                drawerContainerColor = Color(0xFFf0fdf4),
                drawerContentColor = Color(0xFF065f46)
            ) {
                DrawerContent()
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) { padding ->
            //MainContentHome(modifier = Modifier.padding(padding))
            //MainContentHistory(modifier = Modifier.padding(padding))
            MainContentAlert(modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,

    ) {
    Text(
        text = "Opciones",
        fontSize = 24.sp,
        modifier = modifier.padding(16.dp),
        color = Color(0xFF059669) // verde similar al mockup
    )

    HorizontalDivider()

    NavigationDrawerItem(
        icon = {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF059669) // verde similar al mockup
            )
        },
        label = {
            Text(
                "Registros",
                fontSize = 18.sp,
                color = Color(0xFF059669)
            )
        },
        selected = false,
        onClick = { /* Acción de registros */ },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )

    NavigationDrawerItem(
        icon = {
            Icon(
                Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF059669) // verde similar al mockup
            )
        },
        label = {
            Text(
                "Alertas",
                fontSize = 18.sp,
                color = Color(0xFF059669)
            )
        },
        selected = false,
        onClick = { /* Acción de registros */ },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )

    NavigationDrawerItem(
        icon = {
            Icon(
                Icons.Default.Calculate,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF059669) // verde similar al mockup
            )
        },
        label = {
            Text(
                "Calcular abonada",
                fontSize = 18.sp,
                color = Color(0xFF059669)
            )
        },
        selected = false,
        onClick = { /* Acción de registros */ },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier
) {
    //Text(text = "Contenido de la pantalla", modifier = modifier.padding(16.dp))
    //Spacer(modifier = Modifier.height(15.dp))
    //MainContent(modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onOpenDrawer: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.height(75.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF), // Fondo verde claro para la TopBar
            titleContentColor = Color(0xFF059669), // Texto verde oscuro
            actionIconContentColor = Color(0xFF059669),
            navigationIconContentColor = Color(0xFF059669)
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(28.dp)
                    .clickable {
                        onOpenDrawer()
                    }
            )
        },
        title = { Text(text = "Mi Plantio") },
        actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                modifier = Modifier
                    .size(30.dp)
            )

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Perfil de usuario",
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(30.dp)
            )
        }
    )
}