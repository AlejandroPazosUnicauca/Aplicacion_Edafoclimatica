package com.practica.aplicacionedafoclimatica.data.conection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.bufferedReader
import kotlin.io.readText
import kotlin.io.use

class WifiClient(private val host: String, private val port: Int) {

    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        // No hay conexión persistente, solo devolvemos true para indicar que podemos hacer peticiones
        true
    }

    suspend fun receiveData(): String? = fetchLatestData()

    suspend fun readData(): String? = fetchLatestData()

    private suspend fun fetchLatestData(): String? = withContext(Dispatchers.IO) {
        try {
            //val url = URL("http://$host:$port/data/latest")
            val url = URL("http://$host:$port")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                println("Datos recibidos del servidor: $response")
                connection.disconnect()
                response
            } else {
                println("Error HTTP: $responseCode")
                connection.disconnect()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun disconnect() {
        // Nada que cerrar en HTTP
    }
}
