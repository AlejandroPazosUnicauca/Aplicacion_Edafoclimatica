package com.practica.aplicacionedafoclimatica.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.practica.aplicacionedafoclimatica.ui.navigation.AppNavHost
import kotlinx.coroutines.launch

import com.practica.aplicacionedafoclimatica.ui.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val menuItems = listOf(
        AppScreen.Medidas,
        AppScreen.Registros,
        AppScreen.Alertas
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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

                // Listado de ítems del menú
                menuItems.forEach { screen ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                screen.title,
                                fontSize = 18.sp,
                                color = Color(0xFF059669)
                            )
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
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            // Color de fondo cuando está SELECCIONADO (verde oscuro)
                            selectedContainerColor = Color(0x2F059669),
                            // Color del texto/ícono cuando está SELECCIONADO (blanco o verde claro)
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,

                            // Color de fondo cuando NO está SELECCIONADO (transparente)
                            unselectedContainerColor = Color.Transparent,
                            // Color del texto/ícono cuando NO está seleccionado (tu verde original)
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
                        containerColor = Color(0xFFFFFFFF), // Fondo verde claro para la TopBar
                        titleContentColor = Color(0xFF059669), // Texto verde oscuro
                        actionIconContentColor = Color(0xFF059669),
                        navigationIconContentColor = Color(0xFF059669)
                    ),
                    title = { Text("Mi Aplicación Edafoclimática") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavHost(navController = navController)
            }
        }
    }
}