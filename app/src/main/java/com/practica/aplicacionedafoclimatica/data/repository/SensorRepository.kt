package com.practica.aplicacionedafoclimatica.data.repository

import com.practica.aplicacionedafoclimatica.data.conection.WifiClient

class SensorRepository(private val wifiClient: WifiClient) {

    /**
     * Establece la conexión con el servidor del sensor a través del cliente Wi‑Fi.
     *
     * @return true si la conexión se completa correctamente.
     */
    suspend fun connect(): Boolean {
        return wifiClient.connect()
    }

    /**
     * Solicita la última lectura del sensor disponible en la conexión actual.
     *
     * @return contenido del payload recibido o null si la operación falla.
     */
    suspend fun readData(): String? {
        return wifiClient.readData()
    }

    /**
     * Recupera los datos del sensor de la misma forma que la lectura normal para mantener compatibilidad.
     *
     * @return respuesta obtenida del sensor o null si no hay datos disponibles.
     */
    suspend fun receiveData(): String? {
        return wifiClient.receiveData()
    }

    /**
     * Cierra la conexión actual del cliente Wi‑Fi asociado al repositorio.
     */
    fun disconnect() {
        wifiClient.disconnect()
    }
}