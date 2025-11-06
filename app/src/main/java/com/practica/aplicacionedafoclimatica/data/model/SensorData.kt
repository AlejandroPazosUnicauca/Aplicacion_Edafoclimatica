package com.practica.aplicacionedafoclimatica.data.model

data class SensorData(
    val id: Int,
    val fecha: String,
    val Lumenes: Double,
    val Temperatura_ambiente: Double,
    val Humedad_ambiente: Double,
    val Lluvia: Double,
    val Humedad_suelo: Double,
    val Temperatura_suelo: Double,
    val Ph: Float,
    val Fosforo: Double,
    val Nitrogeno: Double,
    val Potasio: Double
)