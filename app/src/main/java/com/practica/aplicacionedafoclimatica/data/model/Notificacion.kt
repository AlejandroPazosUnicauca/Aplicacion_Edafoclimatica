package com.practica.aplicacionedafoclimatica.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Notificacion(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val tipo: TipoNotificacion = TipoNotificacion.ALERTA,
    val hora: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
)

enum class TipoNotificacion {
    INFO,
    ALERTA
}
