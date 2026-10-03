package com.practica.aplicacionedafoclimatica.data.conection

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.bufferedReader
import kotlin.io.readText
import kotlin.io.use

class WifiClient(private val host: String, private val port: Int, private val path: String) {
    /**
     * Construye la URL completa para acceder al sensor usando la dirección, el puerto y la ruta configurada.
     */
    private fun buildUrl(): URL {
        val finalPath = if (path.isBlank() || path.startsWith('/')) path else "/$path"
        return URL("http://$host:$port$finalPath")
    }

    /**
     * Intenta validar la conexión HTTP con el servidor del sensor.
     *
     * @return true si la solicitud responde con código HTTP 200; false en caso contrario.
     */
    suspend fun connect(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val url = buildUrl()
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000

            connection.connect()
            val responseCode = connection.responseCode
            connection.disconnect()

            val isConnected = responseCode == HttpURLConnection.HTTP_OK

            if (isConnected) {
                println("Conexión de prueba exitosa (Código 200).")
            } else {
                println("Conexión de prueba fallida. Código HTTP: $responseCode")
            }

            isConnected
        } catch (e: Exception) {
            println("Error al intentar conexión de prueba: ${e.message}")
            false
        }
    }

    /**
     * Recupera los últimos datos disponibles del sensor mediante una petición HTTP.
     */
    suspend fun receiveData(): String? = fetchLatestData()

    /**
     * Lee y devuelve la respuesta más reciente del endpoint configurado.
     */
    suspend fun readData(): String? = fetchLatestData()

    /**
     * Ejecuta la solicitud HTTP para obtener la última lectura del sensor.
     *
     * @return contenido del cuerpo de la respuesta si la petición tiene éxito; null si falla.
     */
    private suspend fun fetchLatestData(): String? = withContext(Dispatchers.IO) {
        try {
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
                connection.disconnect()
                response
            } else {
                connection.disconnect()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Cierra la conexión actual y deja el cliente listo para reutilizarse.
     */
    fun disconnect() {
    }
}
