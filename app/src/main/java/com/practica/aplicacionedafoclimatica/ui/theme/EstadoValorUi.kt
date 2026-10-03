package com.practica.aplicacionedafoclimatica.ui.theme

import androidx.compose.ui.graphics.Color
import com.practica.aplicacionedafoclimatica.domain.EstadoValor

val EstadoValor.color: Color
    get() = when (this) {
        EstadoValor.OPTIMO -> Color(0xFF059669)
        EstadoValor.ADVERTENCIA -> Color(0xFFF59E0B)
        EstadoValor.PELIGRO -> Color(0xFFDC2626)
    }
