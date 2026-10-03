package com.practica.aplicacionedafoclimatica.domain

data class AlertaInfo(
    val variable: String,
    val valorActual: String,
    val recomendacion: String,
    val estado: EstadoValor
)
