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


class SensorViewModel(application: Application) : AndroidViewModel(application) {
    //private val wifiClient = WifiClient("192.168.137.53", 5001)
    //private val repository = SensorRepository(WifiClient("192.168.137.53", 5001))
    //private val wifiClient = WifiClient("192.168.80.23", 5001)
    private val repository = SensorRepository(WifiClient("192.168.80.23", 5001))
    private val _sensorDataList = MutableStateFlow<List<SensorData>>(emptyList())
    val sensorDataList: StateFlow<List<SensorData>> = _sensorDataList

    private val _status = MutableStateFlow("Esperando conexión...")
    val status: StateFlow<String> = _status

    private val gson = Gson()

    fun connect(){
        viewModelScope.launch {
            val connected = repository.connect()
            if (connected) {
                _status.value = "Conectado al sensor"
                listen()
            } else {
                _status.value = "Error al conectar"
            }
        }
    }

    private fun listen() {
        viewModelScope.launch {
            while (true) {
                val msg = repository.readData()
                msg?.let {
                    println("Datos recibidos: $it")

                    try {
                        // Convertir JSON -> Lista de SensorData
                        val listType = object : TypeToken<List<SensorData>>() {}.type
                        val dataList: List<SensorData> = gson.fromJson(it, listType)

                        // Actualizar flujo (UI observará este valor)
                        _sensorDataList.value = dataList

                    } catch (e: Exception) {
                        println("Error parseando JSON: ${e.message}")
                    }
                }

                delay(5000) // Esperar 5 segundos antes de leer de nuevo
            }
        }
    }


    override fun onCleared() {
        repository.disconnect()
        super.onCleared()
    }


}

