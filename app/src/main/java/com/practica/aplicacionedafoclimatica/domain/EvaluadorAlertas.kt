package com.practica.aplicacionedafoclimatica.domain

import com.practica.aplicacionedafoclimatica.data.model.SensorData

/**
 * Evalúa una lectura del sensor y produce la lista de alertas activas según los rangos agronómicos configurados.
 *
 * @param latestData Última lectura recibida del sensor o null si aún no hay datos.
 * @return Lista de alertas con sugerencias para corregir condiciones críticas.
 */
fun calcularAlertas(latestData: SensorData?): List<AlertaInfo> {
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

    if (tempAmbiente > rangosTempAmbiente.max) listaAlertas.add(
        AlertaInfo("Temperatura Ambiente Alta", "${tempAmbiente}°C", "Aumentar ventilación o proveer sombra.", EstadoValor.PELIGRO)
    )
    if (tempAmbiente < rangosTempAmbiente.min) listaAlertas.add(
        AlertaInfo("Temperatura Ambiente Baja", "${tempAmbiente}°C", "Considerar calefacción o protección térmica.", EstadoValor.PELIGRO)
    )

    if (humedadAmbiente > rangosHumedadAmbiente.max) listaAlertas.add(
        AlertaInfo("Humedad Ambiente Excesiva", "${humedadAmbiente}%", "Mejorar la ventilación.", EstadoValor.PELIGRO)
    )
    if (humedadAmbiente < rangosHumedadAmbiente.min) listaAlertas.add(
        AlertaInfo("Humedad Ambiente Baja", "${humedadAmbiente}%", "Aumentar la humedad con nebulizadores o riego.", EstadoValor.PELIGRO)
    )

    if (humedadAmbiente > rangosHumedadAmbiente.optimoEnd && tempAmbiente > rangosTempAmbiente.optimoEnd) listaAlertas.add(
        AlertaInfo("Riesgo Alto de Roya", "HR: $humedadAmbiente% | Temp: $tempAmbiente°C", "Aumentar monitoreo y ventilación inmediatamente.", EstadoValor.PELIGRO)
    )

    if (lumenes > rangosLumenes.max) listaAlertas.add(
        AlertaInfo("Iluminación Excesiva", "${lumenes} Lux", "Proveer sombra parcial para evitar quemaduras.", EstadoValor.PELIGRO)
    )
    if (lumenes < rangosLumenes.min) listaAlertas.add(
        AlertaInfo("Iluminación Insuficiente", "${lumenes} Lux", "Añadir iluminación artificial complementaria.", EstadoValor.PELIGRO)
    )

    if (humedadSuelo > rangosHumedadSuelo.max) listaAlertas.add(
        AlertaInfo("Exceso de Humedad (Suelo)", "${humedadSuelo}%", "Evaluar drenaje y reducir frecuencia de riego.", EstadoValor.PELIGRO)
    )
    if (humedadSuelo < rangosHumedadSuelo.min) listaAlertas.add(
        AlertaInfo("Sequía (Suelo)", "${humedadSuelo}%", "Incrementar el riego de manera inmediata.", EstadoValor.PELIGRO)
    )

    if (tempSuelo > rangosTempSuelo.max) listaAlertas.add(
        AlertaInfo("Temperatura del Suelo Alta", "${tempSuelo}°C", "Revisar los valores de operación o aplicar acolchado.", EstadoValor.PELIGRO)
    )
    if (tempSuelo < rangosTempSuelo.min) listaAlertas.add(
        AlertaInfo("Temperatura del Suelo Baja", "${tempSuelo}°C", "Revisar los valores de operación o aplicar acolchado.", EstadoValor.PELIGRO)
    )

    if (ph > rangosPh.max) listaAlertas.add(
        AlertaInfo("pH Alto (Alcalinidad)", ph.toString(), "Aplicar azufre elemental o sulfato de aluminio.", EstadoValor.PELIGRO)
    )
    if (ph < rangosPh.min) listaAlertas.add(
        AlertaInfo("pH Bajo (Acidez)", ph.toString(), "Aplicar cal dolomítica o calcita.", EstadoValor.PELIGRO)
    )

    if (nitrogeno < rangosNitrogeno.min) listaAlertas.add(
        AlertaInfo("Nitrógeno Bajo (N)", nitrogeno.toString(), "Aplicar fertilizante nitrogenado.", EstadoValor.PELIGRO)
    )
    if (nitrogeno > rangosNitrogeno.max) listaAlertas.add(
        AlertaInfo("Nitrógeno Excesivo (N)", nitrogeno.toString(), "Revisar la fertilización reciente.", EstadoValor.PELIGRO)
    )

    if (fosforo < rangosFosforo.min) listaAlertas.add(
        AlertaInfo("Fósforo Bajo (P)", fosforo.toString(), "Aplicar fuentes de fósforo.", EstadoValor.PELIGRO)
    )
    if (fosforo > rangosFosforo.max) listaAlertas.add(
        AlertaInfo("Fósforo Excesivo (P)", fosforo.toString(), "Revisar la fertilización reciente.", EstadoValor.PELIGRO)
    )

    if (potasio < rangosPotasio.min) listaAlertas.add(
        AlertaInfo("Potasio Bajo (K)", potasio.toString(), "Aplicar cloruro o sulfato de potasio.", EstadoValor.PELIGRO)
    )
    if (potasio > rangosPotasio.max) listaAlertas.add(
        AlertaInfo("Potasio Excesivo (K)", potasio.toString(), "Revisar la fertilización reciente.", EstadoValor.PELIGRO)
    )

    if (lluvia > rangosLluvia.max) listaAlertas.add(
        AlertaInfo("Precipitación Excesiva", "${lluvia} mm", "Proteger el cultivo del exceso de lluvia.", EstadoValor.PELIGRO)
    )
    if (lluvia < rangosLluvia.min) listaAlertas.add(
        AlertaInfo("Falta de Precipitación", "${lluvia} mm", "Aumentar la frecuencia de riego, si es necesario.", EstadoValor.PELIGRO)
    )

    return listaAlertas
}
