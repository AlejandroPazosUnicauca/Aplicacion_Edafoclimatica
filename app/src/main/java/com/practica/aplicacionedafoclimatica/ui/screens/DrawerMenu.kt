package com.practica.aplicacionedafoclimatica.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.practica.aplicacionedafoclimatica.ui.navigation.AppNavHost
import com.practica.aplicacionedafoclimatica.ui.navigation.AppScreen
import com.practica.aplicacionedafoclimatica.viewmodel.SensorViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(viewModel: SensorViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val alertas by viewModel.alertasActivas.collectAsState()
    var menuVisible by remember { mutableStateOf(false) }

    val horaActual by rememberUpdatedState(
        LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    )

    val menuItems = listOf(
        AppScreen.Medidas,
        AppScreen.Registros,
        AppScreen.Alertas,
        AppScreen.Configuracion
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 🧱 Capa principal: permite superposiciones
    Box(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(280.dp),
                    drawerContainerColor = Color(0xFFf0fdf4),
                    drawerContentColor = Color(0xFF065f46)
                ) {
                    Text(
                        text = "Menú principal",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFF059669)
                    )
                    HorizontalDivider()

                    menuItems.forEach { screen ->
                        if (screen == AppScreen.Configuracion) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }

                        NavigationDrawerItem(
                            label = {
                                Text(screen.title, fontSize = 18.sp, color = Color(0xFF059669))
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color(0xFF059669)
                                )
                            },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    if (screen == AppScreen.Configuracion) {
                                        popUpTo(AppScreen.Configuracion.route) { inclusive = true }
                                    } else {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        restoreState = true
                                    }
                                    launchSingleTop = true
                                }
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0x2F059669),
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedContainerColor = Color.Transparent,
                                unselectedIconColor = Color(0xFF059669),
                                unselectedTextColor = Color(0xFF059669)
                            ),
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFFFFFFF),
                            titleContentColor = Color(0xFF059669),
                            actionIconContentColor = Color(0xFF059669),
                            navigationIconContentColor = Color(0xFF059669)
                        ),
                        title = { Text("Mi Aplicación Edafoclimática") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        },
                        actions = {
                            IconButton(onClick = { menuVisible = !menuVisible }) {
                                BadgedBox(
                                    badge = {
                                        if (alertas.isNotEmpty()) {
                                            Badge(
                                                containerColor = Color.Red,
                                                contentColor = Color.White
                                            ) {
                                                Text("${alertas.size}")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notificaciones",
                                        tint = if (alertas.isNotEmpty()) Color.Red else Color.Gray,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    AppNavHost(navController = navController, viewModel = viewModel)
                }
            }
        }

        // 🪄 Panel flotante sobre todo el contenido
        AnimatedVisibility(
            visible = menuVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .fillMaxSize() // ocupa toda la pantalla para detectar clics fuera
                .zIndex(10f)
        ) {
            // Fondo semitransparente clickeable para cerrar
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { menuVisible = false } // cierra al tocar fuera
            ) {
                // Tarjeta flotante del menú
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 72.dp, end = 16.dp)
                        .width(280.dp)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF212121)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Notificaciones (${alertas.size})",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            IconButton(onClick = { menuVisible = false }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = Color.White
                                )
                            }
                        }

                        if (alertas.isEmpty()) {
                            Text(
                                "Sin alertas activas",
                                color = Color.Gray,
                                modifier = Modifier.padding(8.dp)
                            )
                        } else {
                            LazyColumn {
                                items(alertas) { alerta ->
                                    NotificationItemRow(
                                        mensaje = alerta.recomendacion,
                                        hora = horaActual
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun NotificationItemRow(mensaje: String, hora: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(Color(0xFF2C2C2C), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(mensaje, color = Color.White)
            Text(hora, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
    }
}
