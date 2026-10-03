package com.practica.aplicacionedafoclimatica.domain

/**
 * Determina el estado agronómico de una medición según sus rangos esperados.
 *
 * @param valor Valor analizado.
 * @param rangos Intervalos que definen los umbrales de óptimo, advertencia y peligro.
 * @return Estado que clasifica la medición.
 */
fun obtenerEstadoValor(valor: Float, rangos: RangosAlerta): EstadoValor {
    return when {
        valor >= rangos.optimoStart && valor <= rangos.optimoEnd -> EstadoValor.OPTIMO
        (valor >= rangos.min && valor < rangos.optimoStart) ||
            (valor > rangos.optimoEnd && valor <= rangos.max) -> EstadoValor.ADVERTENCIA
        else -> EstadoValor.PELIGRO
    }
}
