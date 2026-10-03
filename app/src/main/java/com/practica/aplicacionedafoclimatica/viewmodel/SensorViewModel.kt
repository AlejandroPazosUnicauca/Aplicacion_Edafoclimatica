package com.practica.aplicacionedafoclimatica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practica.aplicacionedafoclimatica.data.conection.WifiClient
import com.practica.aplicacionedafoclimatica.data.model.Notificacion
import com.practica.aplicacionedafoclimatica.data.model.SensorData
import com.practica.aplicacionedafoclimatica.data.model.TipoNotificacion
import com.practica.aplicacionedafoclimatica.data.repository.SensorRepository
import com.practica.aplicacionedafoclimatica.domain.AlertaInfo
import com.practica.aplicacionedafoclimatica.domain.calcularAlertas
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SensorViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: SensorRepository? = null
    private val _sensorDataList = MutableStateFlow<List<SensorData>>(emptyList())
    val sensorDataList: StateFlow<List<SensorData>> = _sensorDataList

    private val _status = MutableStateFlow("Esperando configuración de IP...")
    val status: StateFlow<String> = _status

    private val _isConfigured = MutableStateFlow(false)
    val isConfigured: StateFlow<Boolean> = _isConfigured

    private val _alertasActivas = MutableStateFlow<List<AlertaInfo>>(emptyList())
    val alertasActivas: StateFlow<List<AlertaInfo>> = _alertasActivas

    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones

    private val gson = Gson()
    private var isListening = false
    private var connectJob: Job? = null
    private var listenJob: Job? = null

    /**
     * Configura la dirección del sensor, crea el repositorio asociado y inicia la conexión.
     *
     * @param ip Dirección IP del dispositivo sensor.
     * @param port Puerto del servicio HTTP.
     * @param path Ruta opcional del endpoint.
     */
    fun setConnection(ip: String, port: Int, path: String) {
        disconnect()
        repository = SensorRepository(WifiClient(ip, port, path))
        _status.value = "IP configurada. Conectando..."
        _isConfigured.value = true
        connect()
    }

    /**
     * Intenta abrir la conexión con el sensor y, si tiene éxito, inicia el ciclo de escucha.
     */
    fun connect() {
        if (repository == null) {
            _status.value = "Error: IP y Puerto no configurados."
            _isConfigured.value = false
            return
        }
        if (isListening) return

        connectJob?.cancel()
        connectJob = viewModelScope.launch {
            val connected = repository?.connect()
            if (connected == true) {
                _status.value = "Conectado al sensor"
                isListening = true
                listen()
            } else {
                _status.value = "Error al conectar"
                isListening = false
            }
        }
    }

    /**
     * Ejecuta un bucle continuo que solicita datos del sensor y actualiza los estados de la UI.
     */
    private fun listen() {
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            while (isListening) {
                val msg = repository?.readData()
                msg?.let {
                    try {
                        val listType = object : TypeToken<List<SensorData>>() {}.type
                        val dataList: List<SensorData> = gson.fromJson(it, listType)
                        _sensorDataList.value = dataList
                        val alertas = calcularAlertas(dataList.firstOrNull())
                        _alertasActivas.value = alertas
                        manejarNotificaciones(alertas)
                    } catch (e: Exception) {
                        _status.value = "Error en datos"
                        println("Excepcion: $e")
                    }
                }
                delay(5000)
            }
        }
    }

    /**
     * Actualiza la lista de notificaciones con las alertas activas y elimina las ya resueltas.
     *
     * @param alertas Lista de alertas detectadas en la última lectura del sensor.
     */
    private fun manejarNotificaciones(alertas: List<AlertaInfo>) {
        val nuevasNotificaciones = mutableListOf<Notificacion>()
        val actuales = _notificaciones.value.toMutableList()

        alertas.forEach { alerta ->
            if (actuales.none { it.titulo == alerta.variable }) {
                nuevasNotificaciones.add(
                    Notificacion(
                        titulo = alerta.variable,
                        descripcion = alerta.recomendacion,
                        tipo = TipoNotificacion.ALERTA
                    )
                )
            }
        }

        val actualizadas = actuales.filter { noti ->
            alertas.any { it.variable == noti.titulo }
        }

        _notificaciones.value = actualizadas + nuevasNotificaciones
    }

    /**
     * Detiene cualquier tarea activa y desconecta el repositorio asociado.
     */
    private fun disconnect() {
        isListening = false
        connectJob?.cancel()
        listenJob?.cancel()
        repository?.disconnect()
        repository = null
        _status.value = "Desconectado"
    }

    /**
     * Se ejecuta cuando el ViewModel se destruye para cerrar la sesión de escucha del sensor.
     */
    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}
