package com.cordova.gianella.innovacionneuroluxkids

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

object MqttClientManager {

    private const val TAG = "MQTT_MANAGER"
    private lateinit var mqttClient: MqttAndroidClient

    // Variable de estado para saber si estamos conectados
    var isConnected: Boolean = false
        private set // Solo se puede modificar desde dentro de esta clase (en los callbacks)

    // Configuración del Broker (asegúrate de que sea accesible desde tu red)
    private const val MQTT_BROKER_URL = "tcp://test.mosquitto.org:1883"
    private const val MQTT_USERNAME = ""
    private const val MQTT_PASSWORD = ""

    fun connect(context: Context) {
        if (::mqttClient.isInitialized && mqttClient.isConnected) {
            Log.d(TAG, "El cliente MQTT ya está conectado.")
            return
        }

        Log.d(TAG, "Iniciando conexión a MQTT...")
        val clientId = MqttClient.generateClientId()
        mqttClient = MqttAndroidClient(context.applicationContext, MQTT_BROKER_URL, clientId)

        // Configurar los callbacks para actualizar nuestro estado de conexión
        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.i(TAG, "¡CONEXIÓN COMPLETADA! -> Conectado a: $serverURI")
                isConnected = true // ¡CONECTADOS!
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e(TAG, "¡CONEXIÓN PERDIDA! -> Causa: ${cause?.toString()}")
                isConnected = false // ¡DESCONECTADOS!
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                // No se usa en este caso
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // No se usa en este caso
            }
        })

        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
            if (MQTT_USERNAME.isNotBlank()) {
                userName = MQTT_USERNAME
                password = MQTT_PASSWORD.toCharArray()
            }
        }

        try {
            Log.d(TAG, "Intentando conectar al broker: $MQTT_BROKER_URL")
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    // Este onSuccess solo indica que la solicitud de conexión se envió.
                    // La confirmación real llega en 'connectComplete'.
                    Log.d(TAG, "Solicitud de conexión a MQTT enviada exitosamente.")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "¡FALLO AL ENVIAR SOLICITUD DE CONEXIÓN! -> Razón: ${exception?.toString()}")
                    isConnected = false
                }
            })
        } catch (e: MqttException) {
            Log.e(TAG, "¡ERROR CRÍTICO! -> Excepción MqttException: ${e.message}")
            isConnected = false
            e.printStackTrace()
        }
    }

    fun publish(topic: String, msg: String) {
        // Ahora usamos nuestra variable de estado para la verificación
        if (!isConnected) {
            Log.e(TAG, "PUBLICACIÓN FALLIDA -> Cliente no conectado. Imposible enviar: '$msg' al topic '$topic'")
            return
        }

        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = 1 // Calidad de servicio 1: asegura que el mensaje llegue al menos una vez
            message.isRetained = false
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.i(TAG, "PUBLICACIÓN EXITOSA -> Topic: '$topic', Mensaje: '$msg'")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "PUBLICACIÓN FALLIDA -> Topic: '$topic', Razón: ${exception?.toString()}")
                }
            })
        } catch (e: MqttException) {
            Log.e(TAG, "PUBLICACIÓN FALLIDA -> Excepción MqttException: ${e.message}")
            e.printStackTrace()
        }
    }
}