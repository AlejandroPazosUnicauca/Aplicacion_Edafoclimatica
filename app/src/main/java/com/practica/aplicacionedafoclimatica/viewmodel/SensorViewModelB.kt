package com.practica.aplicacionedafoclimatica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.practica.aplicacionedafoclimatica.data.conection.WifiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SensorViewModelB(application: Application) : AndroidViewModel(application) {
    private val wifiClient = WifiClient("192.168.137.53", 5001)
    private val _data = MutableStateFlow("Esperando datos...")
    val data: StateFlow<String> = _data

    fun connect(){
        viewModelScope.launch {
            val connected = wifiClient.connect()
            if (connected) {
                _data.value = "Conectado al sensor"
                val data = wifiClient.receiveData() // método que retorna String o JSON
                listen()
            } else {
                _data.value = "Error al conectar"
            }
        }
    }

    private fun listen() {
        viewModelScope.launch {
            while (true) {
                val msg = wifiClient.readData()
                println("Datos actualizados: $msg")
                msg?.let { _data.value = it }
                delay(5000)
            }
        }
    }


    override fun onCleared() {
        wifiClient.disconnect()
        super.onCleared()
    }


}

