package com.practica.aplicacionedafoclimatica.domain

/**
 * Umbrales de una variable.
 * [optimoStart]–[optimoEnd]: óptimo.
 * [min]–[optimoStart] y [optimoEnd]–[max]: advertencia.
 * Fuera de [min]–[max]: peligro.
 * [maxAbsoluto] es la escala del medidor en pantalla, no un umbral de alerta.
 */
data class RangosAlerta(
    val min: Float,
    val optimoStart: Float,
    val optimoEnd: Float,
    val max: Float,
    val maxAbsoluto: Float
)
