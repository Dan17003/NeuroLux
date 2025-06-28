package com.cordova.gianella.innovacionneuroluxkids

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TerceraPaginaActivity : BaseActivity() {

    // Variable para guardar la fecha y hora seleccionadas
    private val calendar = Calendar.getInstance()

    // Referencias a las vistas
    private lateinit var cardProgramarHeader: CardView
    private lateinit var expandableLayout: LinearLayout
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var tvSelectDate: TextView
    private lateinit var tvSelectTime: TextView
    private lateinit var programSeekBar: SeekBar
    private lateinit var iconArrowDown: ImageView

    // Botones de modo
    private lateinit var btnJuego: LinearLayout
    private lateinit var btnEstudio: LinearLayout
    private lateinit var btnDormir: LinearLayout

    // ---- MÉTODOS OBLIGATORIOS DE BaseActivity ----

    override fun getLayoutResourceId(): Int {
        return R.layout.tercera_pagina
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.nav_routines
    }

    // ---- LÓGICA DE LA ACTIVIDAD ----

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar vistas
        initializeViews()

        // Configurar listeners
        setupExpandableCard()
        setupDateTimePickers()
        setupIntensitySlider()
        setupModeButtons()
    }

    private fun initializeViews() {
        // Referencias del layout expandible
        cardProgramarHeader = findViewById(R.id.cardProgramarHeader)
        expandableLayout = findViewById(R.id.expandableLayout)
        rootLayout = findViewById(R.id.root_layout_modo)
        iconArrowDown = findViewById(R.id.iconArrowDown)

        // Referencias para fecha y hora
        tvSelectDate = findViewById(R.id.tvSelectDate)
        tvSelectTime = findViewById(R.id.tvSelectTime)

        // SeekBar de intensidad
        programSeekBar = findViewById(R.id.programSeekBar)

        // Botones de modo
        btnJuego = findViewById(R.id.btnJuego)
        btnEstudio = findViewById(R.id.btnEstudio)
        btnDormir = findViewById(R.id.btnDormir)
    }

    private fun setupExpandableCard() {
        cardProgramarHeader.setOnClickListener {
            TransitionManager.beginDelayedTransition(rootLayout)

            val isVisible = expandableLayout.visibility == View.VISIBLE
            expandableLayout.visibility = if (isVisible) View.GONE else View.VISIBLE

            // Rotar la flecha
            val rotation = if (isVisible) 0f else 180f
            iconArrowDown.animate().rotation(rotation).setDuration(200).start()
        }
    }

    private fun setupDateTimePickers() {
        // Inicializar con fecha y hora actuales
        updateDateLabel()
        updateTimeLabel()

        tvSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        tvSelectTime.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun setupIntensitySlider() {
        programSeekBar.max = 255
        programSeekBar.progress = 128 // Valor por defecto

        programSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Opcionalmente mostrar el valor en tiempo real
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val intensityValue = seekBar?.progress ?: 128
                // Aquí puedes manejar el valor de intensidad seleccionado
                println("Intensidad programada: $intensityValue")
            }
        })
    }

    private fun setupModeButtons() {
        btnJuego.setOnClickListener {
            handleModeSelection("juego")
        }

        btnEstudio.setOnClickListener {
            handleModeSelection("estudio")
        }

        btnDormir.setOnClickListener {
            handleModeSelection("dormir")
        }
    }

    private fun handleModeSelection(mode: String) {
        // Resetear todos los botones
        resetModeButtons()

        // Marcar el botón seleccionado (opcional: cambiar color o estado)
        when (mode) {
            "juego" -> {
                // btnJuego.setBackgroundResource(R.drawable.button_background_selected)
                publishMqttMessage("neuroluxkids/mode/control", "game")
            }
            "estudio" -> {
                // btnEstudio.setBackgroundResource(R.drawable.button_background_selected)
                publishMqttMessage("neuroluxkids/mode/control", "study")
            }
            "dormir" -> {
                // btnDormir.setBackgroundResource(R.drawable.button_background_selected)
                publishMqttMessage("neuroluxkids/mode/control", "sleep")
            }
        }

        println("Modo seleccionado: $mode")
    }

    private fun resetModeButtons() {
        // Resetear el fondo de todos los botones al estado normal
        // btnJuego.setBackgroundResource(R.drawable.button_background_blue)
        // btnEstudio.setBackgroundResource(R.drawable.button_background_blue)
        // btnDormir.setBackgroundResource(R.drawable.button_background_blue)
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()

            // Opcional: Validar que la fecha no sea en el pasado
            if (calendar.timeInMillis < System.currentTimeMillis()) {
                // Mostrar mensaje de advertencia si quieres
            }
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Opcional: Establecer fecha mínima como hoy
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0) // Resetear segundos
            updateTimeLabel()
        }

        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Usar formato de 24 horas
        ).show()
    }

    private fun updateDateLabel() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        tvSelectDate.text = sdf.format(calendar.time)
    }

    private fun updateTimeLabel() {
        val format = "HH:mm"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        tvSelectTime.text = sdf.format(calendar.time)
    }

    private fun publishMqttMessage(topic: String, message: String) {
        // AQUÍ DEBES INTEGRAR TU LÓGICA DE PUBLICACIÓN CON LA LIBRERÍA MQTT (PAHO)
        // Este es un placeholder para demostrar dónde va la llamada.
        println("MQTT Publicando -> Topic: $topic, Mensaje: '$message'")

        // Ejemplo de cómo se vería con un cliente MQTT real:
        // if (mqttClient.isConnected()) {
        //     try {
        //         val mqttMessage = MqttMessage(message.toByteArray())
        //         mqttClient.publish(topic, mqttMessage)
        //     } catch (e: MqttException) {
        //         e.printStackTrace()
        //     }
        // }
    }

    // Función auxiliar para programar la rutina
    private fun scheduleRoutine() {
        val selectedDateTime = calendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        if (selectedDateTime <= currentTime) {
            // Mostrar mensaje de error
            println("Error: La fecha y hora deben ser en el futuro")
            return
        }

        val intensity = programSeekBar.progress
        val timeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        println("Rutina programada para: ${timeFormat.format(calendar.time)}")
        println("Intensidad: $intensity")

        // Aquí puedes implementar la lógica para programar la rutina
        // Por ejemplo, usando AlarmManager o WorkManager

        // Opcional: Mostrar mensaje de confirmación
        // Toast.makeText(this, "Rutina programada exitosamente", Toast.LENGTH_SHORT).show()
    }

    // Función para obtener la fecha y hora seleccionadas (útil para otras clases)
    fun getSelectedDateTime(): Calendar {
        return calendar.clone() as Calendar
    }

    fun getSelectedIntensity(): Int {
        return programSeekBar.progress
    }
}