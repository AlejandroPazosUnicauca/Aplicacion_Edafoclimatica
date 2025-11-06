package com.practica.aplicacionedafoclimatica.data.repository

import com.practica.aplicacionedafoclimatica.data.conection.WifiClient

class SensorRepository(private val wifiClient: WifiClient) {

    suspend fun connect(): Boolean {
        return wifiClient.connect()
    }

    suspend fun readData(): String? {
        return wifiClient.readData()
    }

    suspend fun receiveData(): String? {
        return wifiClient.receiveData()
    }

    fun disconnect() {
        wifiClient.disconnect()
    }
}