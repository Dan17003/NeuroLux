package com.cordova.gianella.innovacionneuroluxkids

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Carga tu layout de inicio con el robot
        setContentView(R.layout.inicio)

        // 2. Usamos un Handler para crear un retraso
        // ... (resto del código igual)
        Handler(Looper.getMainLooper()).postDelayed({
            // Cambiamos aquí a dónde nos dirigimos
            val intent = Intent(this@MainActivity, ControlLuzActivity::class.java) // <--- ¡CAMBIA ESTA LÍNEA!

            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIMEOUT)
// ... (resto del código igual)
    }

    companion object {
        // Duración de la pantalla de bienvenida en milisegundos (ej: 3000 = 3 segundos)
        private const val SPLASH_SCREEN_TIMEOUT = 3000L // Es buena práctica añadir una 'L' para indicar que es un tipo Long
    }
}