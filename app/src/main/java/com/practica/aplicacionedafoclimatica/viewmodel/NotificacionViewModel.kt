package com.practica.aplicacionedafoclimatica.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NotificationItem(
    val message: String,
    val timestamp: String = getCurrentTime()
)

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}

class NotificacionViewModel : ViewModel() {

    // Lista de notificaciones activas
    private val _notifications = mutableStateListOf<NotificationItem>()
    val notifications: List<NotificationItem> get() = _notifications

    // Indica si el menú de notificaciones está desplegado
    var isMenuExpanded by mutableStateOf(false)

    // Cantidad de notificaciones (para mostrar en el badge)
    val count: Int get() = _notifications.size

    /** Agrega una nueva notificación */
    fun addNotification(message: String) {
        _notifications.add(NotificationItem(message))
    }

    /** Elimina una notificación específica */
    fun removeNotification(notification: NotificationItem) {
        _notifications.remove(notification)
    }

    /** Limpia todas las notificaciones (por ejemplo, si las lecturas son normales) */
    fun clearAll() {
        _notifications.clear()
    }

    /** Alterna el estado del menú desplegable */
    fun toggleMenu() {
        isMenuExpanded = !isMenuExpanded
    }

    /** Cierra el menú desplegable */
    fun closeMenu() {
        isMenuExpanded = false
    }
}
