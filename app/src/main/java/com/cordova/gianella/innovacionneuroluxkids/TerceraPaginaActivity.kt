package com.cordova.gianella.innovacionneuroluxkids

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TerceraPaginaActivity : AppCompatActivity() {
    // Variable para guardar la fecha y hora seleccionadas
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tercera_pagina)

        // Referencias a las vistas del layout
        val btnBack: ImageView = findViewById(R.id.btnBack)
        val cardProgramarHeader: CardView = findViewById(R.id.cardProgramarHeader)
        val expandableLayout: LinearLayout = findViewById(R.id.expandableLayout)
        val rootLayout: ConstraintLayout = findViewById(R.id.root_layout_modo)

        // NUEVAS referencias a los TextViews para fecha y hora
        val tvSelectDate: TextView = findViewById(R.id.tvSelectDate)
        val tvSelectTime: TextView = findViewById(R.id.tvSelectTime)

        // --- LÓGICA PARA EXPANDIR Y COLAPSAR ---
        cardProgramarHeader.setOnClickListener {
            TransitionManager.beginDelayedTransition(rootLayout)
            expandableLayout.visibility = if (expandableLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // --- LÓGICA PARA SELECCIONAR FECHA ---
        tvSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        // --- LÓGICA PARA SELECCIONAR HORA ---
        tvSelectTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }
        // ... (listeners de los botones de modo)
    }

    private fun showDatePickerDialog() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePickerDialog() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
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
        val tvSelectDate: TextView = findViewById(R.id.tvSelectDate)
        tvSelectDate.text = sdf.format(calendar.time)
    }

    private fun updateTimeLabel() {
        val format = "HH:mm"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val tvSelectTime: TextView = findViewById(R.id.tvSelectTime)
        tvSelectTime.text = sdf.format(calendar.time)
    }
}
