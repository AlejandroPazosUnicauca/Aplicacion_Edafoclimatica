package com.practica.aplicacionedafoclimatica.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.practica.aplicacionedafoclimatica.data.model.Notificacion
import com.practica.aplicacionedafoclimatica.data.model.TipoNotificacion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NotificationDropdown(
    notificaciones: List<Notificacion>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.TopEnd) {

        // Ícono con contador de notificaciones
        BadgedBox(
            badge = {
                if (notificaciones.isNotEmpty()) {
                    Badge(containerColor = Color.Red) {
                        Text(
                            text = notificaciones.size.toString(),
                            color = Color.White
                        )
                    }
                }
            }
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = if (notificaciones.isNotEmpty()) Color.Red else Color.Gray
                )
            }
        }

        // Menú desplegable animado
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Popup(
                alignment = Alignment.TopEnd,
                properties = PopupProperties(focusable = false)
            ) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(end = 8.dp, top = 45.dp)
                        .background(Color.White),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (notificaciones.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Sin notificaciones")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 250.dp)
                                .padding(8.dp)
                        ) {
                            items(notificaciones) { noti ->
                                NotificationItem(noti)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(noti: Notificacion) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (noti.tipo) {
                TipoNotificacion.ALERTA -> Icons.Default.Warning
                TipoNotificacion.INFO -> Icons.Default.Info
            },
            contentDescription = null,
            tint = if (noti.tipo == TipoNotificacion.ALERTA) Color.Red else Color.Blue,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = noti.titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Text(
                text = noti.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(
            text = noti.hora,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}
