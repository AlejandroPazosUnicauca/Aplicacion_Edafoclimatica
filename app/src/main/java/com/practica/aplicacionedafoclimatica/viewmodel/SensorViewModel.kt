package com.practica.aplicacionedafoclimatica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practica.aplicacionedafoclimatica.data.model.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.practica.aplicacionedafoclimatica.data.repository.SensorRepository
import com.practica.aplicacionedafoclimatica.data.conection.WifiClient
import kotlinx.coroutines.Job


class SensorViewModel(application: Application) : AndroidViewModel(application) {
    //private val repository = SensorRepository(WifiClient("192.168.4.2", 5001))
    private var repository : SensorRepository? = null
    private val _sensorDataList = MutableStateFlow<List<SensorData>>(emptyList())
    val sensorDataList: StateFlow<List<SensorData>> = _sensorDataList

    private val _status = MutableStateFlow("Esperando configuración de IP...")
    val status: StateFlow<String> = _status

    // NUEVO: Estado para saber si la IP/Puerto ha sido configurada
    private val _isConfigured = MutableStateFlow(false)
    val isConfigured: StateFlow<Boolean> = _isConfigured

    private val gson = Gson()

    // Controladores de Corutinas
    private var isListening = false
    private var connectJob: Job? = null
    private var listenJob: Job? = null

    fun setConnection(ip: String, port: Int) {
        // Desconecta cualquier repositorio anterior
        disconnect()

        // Crea el nuevo repositorio con la IP y Puerto
        repository = SensorRepository(WifiClient(ip, port))
        _status.value = "IP configurada. Conectando..."

        // Marcamos como configurado después de que el usuario proporciona IP/Puerto
        _isConfigured.value = true

        // Inicia la conexión automáticamente
        connect()
    }

    fun connect(){
        // No conectar si el repositorio no está inicializado
        if (repository == null) {
            _status.value = "Error: IP y Puerto no configurados."
            _isConfigured.value = false
            return
        }

        // No reconectar si ya está escuchando
        if (isListening) return

        connectJob?.cancel() // Cancela intentos anteriores

        connectJob = viewModelScope.launch {
            val connected = repository?.connect() // Llamada segura
            if (connected == true) {
                _status.value = "Conectado al sensor"
                isListening = true
                listen() // Inicia el bucle de escucha
            } else {
                _status.value = "Error al conectar"
                isListening = false
            }
        }
    }

    private fun listen() {
        listenJob?.cancel() // Cancela bucles anteriores
        listenJob = viewModelScope.launch {
            while (isListening) {
                val msg = repository?.readData() // Llamada segura
                msg?.let {
                    println("Datos recibidos: $it")
                    try {
                        val listType = object : TypeToken<List<SensorData>>() {}.type
                        val dataList: List<SensorData> = gson.fromJson(it, listType)
                        _sensorDataList.value = dataList
                    } catch (e: Exception) {
                        println("Error parseando JSON: ${e.message}")
                        _status.value = "Error en datos"
                    }
                }
                delay(5000)
            }
        }
    }


    private fun disconnect() {
        isListening = false // Detiene el bucle en listen()
        connectJob?.cancel()
        listenJob?.cancel()
        repository?.disconnect() // Cierra el socket
        repository = null // Limpia el repositorio
        _status.value = "Desconectado"
    }

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}

