package com.practica.aplicacionedafoclimatica.data.conection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.bufferedReader
import kotlin.io.readText
import kotlin.io.use
import android.util.Log

class WifiClient(private val host: String, private val port: Int, private val path: String) {
    // Función auxiliar para construir la URL completa con la ruta
    private fun buildUrl(): URL {
        // Aseguramos que la ruta comience con '/' si no está vacía
        val finalPath = if (path.isBlank() || path.startsWith('/')) path else "/$path"

        // Si es una prueba de conexión, usamos solo la raíz para una verificación rápida
        //val effectivePath = if (isTest) "/" else finalPath

        return URL("http://$host:$port$finalPath")
    }

    /**
     * Intenta establecer la URL y realizar una conexión de prueba para validar
     * que la IP y el Puerto son válidos y que el servidor está escuchando.
     * @return True si la prueba HTTP fue exitosa (código 200).
     */
    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val url = buildUrl()
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            // Usamos un timeout corto solo para la prueba de conexión
            connection.connectTimeout = 3000 // 3 segundos
            connection.readTimeout = 3000  // 3 segundos

            connection.connect() // Intentar la conexión física
            val responseCode = connection.responseCode
            connection.disconnect()

            // Solo consideramos la conexión exitosa si recibimos un código 200 (OK)
            val isConnected = responseCode == HttpURLConnection.HTTP_OK

            if (isConnected) {
                println("Conexión de prueba exitosa (Código 200).")
            } else {
                println("Conexión de prueba fallida. Código HTTP: $responseCode")
            }

            isConnected

        } catch (e: Exception) {
            // Capturamos cualquier error de red real: UnknownHost, Timeout, etc.
            println("Error al intentar conexión de prueba: ${e.message}")
            // e.printStackTrace() // Solo si es necesario
            false // Si hay una excepción, la conexión falla.
        }
    }

    suspend fun receiveData(): String? = fetchLatestData()

    suspend fun readData(): String? = fetchLatestData()

    private suspend fun fetchLatestData(): String? = withContext(Dispatchers.IO) {
        try {
            //val url = URL("http://$host:$port/data/latest")
            val url = buildUrl()
            print("Solicitando datos a la URL: $url\n")
            Log.i("MiTag", "Solicitando datos a la URL: $url")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                //println("Datos recibidos del servidor: $response")
                connection.disconnect()
                response
            } else {
                //println("Error HTTP: $responseCode")
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
