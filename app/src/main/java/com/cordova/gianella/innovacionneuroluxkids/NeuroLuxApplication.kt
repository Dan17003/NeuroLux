package com.cordova.gianella.innovacionneuroluxkids

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class NeuroLuxApplication : Application(), ViewModelStoreOwner {


    override val viewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }
    override fun onCreate() {
        super.onCreate()
        // Iniciar la conexión MQTT al crear la aplicación
        MqttClientManager.connect(this)
    }
}



