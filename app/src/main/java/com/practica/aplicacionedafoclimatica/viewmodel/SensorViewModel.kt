package com.practica.aplicacionedafoclimatica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practica.aplicacionedafoclimatica.data.model.SensorData
import com.practica.aplicacionedafoclimatica.data.model.Notificacion
import com.practica.aplicacionedafoclimatica.data.model.TipoNotificacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.practica.aplicacionedafoclimatica.data.repository.SensorRepository
import com.practica.aplicacionedafoclimatica.data.conection.WifiClient
import com.practica.aplicacionedafoclimatica.ui.screens.medidas.*
import kotlinx.coroutines.Job

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

    // NUEVO: flujo para notificaciones
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones

    private val gson = Gson()
    private var isListening = false
    private var connectJob: Job? = null
    private var listenJob: Job? = null

    fun setConnection(ip: String, port: Int, path: String) {
        disconnect()
        repository = SensorRepository(WifiClient(ip, port, path))
        _status.value = "IP configurada. Conectando..."
        _isConfigured.value = true
        connect()
    }

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

    private fun manejarNotificaciones(alertas: List<AlertaInfo>) {
        val nuevasNotificaciones = mutableListOf<Notificacion>()
        val actuales = _notificaciones.value.toMutableList()

        // Agregar nuevas notificaciones solo si no existen
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

        // Eliminar notificaciones resueltas
        val actualizadas = actuales.filter { noti ->
            alertas.any { it.variable == noti.titulo }
        }

        _notificaciones.value = actualizadas + nuevasNotificaciones
    }

    private fun calcularAlertas(latestData: SensorData?): List<AlertaInfo> {
        val listaAlertas = mutableListOf<AlertaInfo>()
        if (latestData == null) return emptyList()

        val lumenes = latestData.Lumenes.toFloat()
        val tempAmbiente = latestData.Temperatura_ambiente.toFloat()
        val humedadAmbiente = latestData.Humedad_ambiente.toFloat()
        val lluvia = latestData.Lluvia.toFloat()
        val humedadSuelo = latestData.Humedad_suelo.toFloat()
        val tempSuelo = latestData.Temperatura_suelo.toFloat()
        val ph = latestData.Ph
        val fosforo = latestData.Fosforo.toFloat()
        val nitrogeno = latestData.Nitrogeno.toFloat()
        val potasio = latestData.Potasio.toFloat()

        // --- Alertas de Temperatura Ambiente ---
        if (tempAmbiente > rangosTempAmbiente.max) listaAlertas.add(
            AlertaInfo("Temperatura Ambiente Alta", "${tempAmbiente}°C", "Aumentar ventilación o proveer sombra.", EstadoValor.PELIGRO)
        )
        if (tempAmbiente < rangosTempAmbiente.min) listaAlertas.add(
            AlertaInfo("Temperatura Ambiente Baja", "${tempAmbiente}°C", "Considerar calefacción o protección térmica.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Humedad Ambiente ---
        if (humedadAmbiente > rangosHumedadAmbiente.max) listaAlertas.add(
            AlertaInfo("Humedad Ambiente Excesiva", "${humedadAmbiente}%", "Mejorar la ventilación.", EstadoValor.PELIGRO)
        )
        if (humedadAmbiente < rangosHumedadAmbiente.min) listaAlertas.add(
            AlertaInfo("Humedad Ambiente Baja", "${humedadAmbiente}%", "Aumentar la humedad con nebulizadores o riego.", EstadoValor.PELIGRO)
        )

        // --- ALERTA ESPECIAL: Riesgo de Roya (T y HR altas) ---
        if (humedadAmbiente > rangosHumedadAmbiente.optimoEnd && tempAmbiente > rangosTempAmbiente.optimoEnd) listaAlertas.add(
            AlertaInfo("Riesgo Alto de Roya", "HR: $humedadAmbiente% | Temp: $tempAmbiente°C", "Aumentar monitoreo y ventilación inmediatamente.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Lúmenes ---
        if (lumenes > rangosLumenes.max) listaAlertas.add(
            AlertaInfo("Iluminación Excesiva", "${lumenes} Lux", "Proveer sombra parcial para evitar quemaduras.", EstadoValor.PELIGRO)
        )
        if (lumenes < rangosLumenes.min) listaAlertas.add(
            AlertaInfo("Iluminación Insuficiente", "${lumenes} Lux", "Añadir iluminación artificial complementaria.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Humedad del Suelo ---
        if (humedadSuelo > rangosHumedadSuelo.max) listaAlertas.add(
            AlertaInfo("Exceso de Humedad (Suelo)", "${humedadSuelo}%", "Evaluar drenaje y reducir frecuencia de riego.", EstadoValor.PELIGRO)
        )
        if (humedadSuelo < rangosHumedadSuelo.min) listaAlertas.add(
            AlertaInfo("Sequía (Suelo)", "${humedadSuelo}%", "Incrementar el riego de manera inmediata.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Temperatura del Suelo ---
        // Asumiendo que el rango óptimo es 19°C - 22°C (rangosTempSuelo.optimoStart y rangosTempSuelo.optimoEnd)
        if (tempSuelo > rangosTempSuelo.max) listaAlertas.add(
            AlertaInfo("Temperatura del Suelo Alta", "${tempSuelo}°C", "Revisar los valores de operación o aplicar acolchado.", EstadoValor.PELIGRO)
        )
        if (tempSuelo < rangosTempSuelo.min) listaAlertas.add(
            AlertaInfo("Temperatura del Suelo Baja", "${tempSuelo}°C", "Revisar los valores de operación o aplicar acolchado.", EstadoValor.PELIGRO)
        )

        // --- Alertas de pH ---
        if (ph > rangosPh.max) listaAlertas.add(
            AlertaInfo("pH Alto (Alcalinidad)", ph.toString(), "Aplicar azufre elemental o sulfato de aluminio.", EstadoValor.PELIGRO)
        )
        if (ph < rangosPh.min) listaAlertas.add(
            AlertaInfo("pH Bajo (Acidez)", ph.toString(), "Aplicar cal dolomítica o calcita.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Nitrógeno (N) ---
        if (nitrogeno < rangosNitrogeno.min) listaAlertas.add(
            AlertaInfo("Nitrógeno Bajo (N)", nitrogeno.toString(), "Aplicar fertilizante nitrogenado.", EstadoValor.PELIGRO)
        )
        // Nota: No se requiere Nitrogeno Max, ya que la deficiencia es el principal problema.
        if (nitrogeno > rangosNitrogeno.max) listaAlertas.add(
            AlertaInfo("Nitrógeno Excesivo (N)", nitrogeno.toString(), "Revisar la fertilización reciente.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Fósforo (P) ---
        if (fosforo < rangosFosforo.min) listaAlertas.add(
            AlertaInfo("Fósforo Bajo (P)", fosforo.toString(), "Aplicar fuentes de fósforo.", EstadoValor.PELIGRO)
        )
        if (fosforo > rangosFosforo.max) listaAlertas.add(
            AlertaInfo("Fósforo Excesivo (P)", fosforo.toString(), "Revisar la fertilización reciente.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Potasio (K) ---
        if (potasio < rangosPotasio.min) listaAlertas.add(
            AlertaInfo("Potasio Bajo (K)", potasio.toString(), "Aplicar cloruro o sulfato de potasio.", EstadoValor.PELIGRO)
        )
        if (potasio > rangosPotasio.max) listaAlertas.add(
            AlertaInfo("Potasio Excesivo (K)", potasio.toString(), "Revisar la fertilización reciente.", EstadoValor.PELIGRO)
        )

        // --- Alertas de Lluvia ---
        if (lluvia > rangosLluvia.max) listaAlertas.add(
            AlertaInfo("Precipitación Excesiva", "${lluvia} mm", "Proteger el cultivo del exceso de lluvia.", EstadoValor.PELIGRO)
        )
        if (lluvia < rangosLluvia.min) listaAlertas.add(
            AlertaInfo("Falta de Precipitación", "${lluvia} mm", "Aumentar la frecuencia de riego, si es necesario.", EstadoValor.PELIGRO)
        )

        return listaAlertas
    }

    private fun disconnect() {
        isListening = false
        connectJob?.cancel()
        listenJob?.cancel()
        repository?.disconnect()
        repository = null
        _status.value = "Desconectado"
    }

    override fun onCleared() {
        disconnect()
        super.onCleared()
    }
}
