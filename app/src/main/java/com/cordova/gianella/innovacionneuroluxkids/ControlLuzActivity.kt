package com.cordova.gianella.innovacionneuroluxkids

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ControlLuzActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_luz)

        val btnSettings = findViewById<ImageView>(R.id.btnSettings)

        btnSettings.setOnClickListener {
            val intent = Intent(this, TerceraPaginaActivity::class.java)
            startActivity(intent)
        }

        // Dentro de tu ControlLuzActivity.kt, en el método onCreate:

        val seekBar: SeekBar = findViewById(R.id.seekBar1)
        val tvPercentage: TextView = findViewById(R.id.tvPercentage)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el texto con el nuevo valor del progreso
                tvPercentage.text = "$progress%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }


}