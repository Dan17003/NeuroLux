package com.cordova.gianella.innovacionneuroluxkids

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.button.MaterialButton

class HomeActivity : BaseActivity() {

    private lateinit var lampViewModel: LampStateViewModel

    override fun getLayoutResourceId(): Int {
        return R.layout.content_main
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.nav_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lampViewModel = ViewModelProvider(this.applicationContext as ViewModelStoreOwner).get(LampStateViewModel::class.java)

        val btnGoToControl: MaterialButton = findViewById(R.id.btnGoToControl)
        btnGoToControl.setOnClickListener {
            startActivity(Intent(this, ControlLuzActivity::class.java))
        }

        lampViewModel.state.observe(this) { newState ->
            updateStatusUI(newState)
        }
    }

    private fun updateStatusUI(state: LampState) {
        val tvLampStatus: TextView = findViewById(R.id.tvLampStatus)
        val tvIntensityStatus: TextView = findViewById(R.id.tvIntensityStatus)
        val colorStatusView: View = findViewById(R.id.colorStatusView)

        if (state.isOn) {
            tvLampStatus.text = "Encendida - ${state.colorName}"
            val intensityPercent = (state.intensity / 255.0 * 100).toInt()
            tvIntensityStatus.text = "$intensityPercent%"

            val colorRes = when (state.colorCode) {
                'r' -> R.color.red
                'v' -> R.color.green
                'a' -> R.color.blue
                'm' -> R.color.magenta
                'c' -> R.color.cyan
                'n' -> R.color.orange
                else -> android.R.color.darker_gray
            }
            val drawable = colorStatusView.background.mutate() as? GradientDrawable
            drawable?.setColor(ContextCompat.getColor(this, colorRes))
        } else {
            tvLampStatus.text = "Apagada"
            tvIntensityStatus.text = "0%"
            val drawable = colorStatusView.background.mutate() as? GradientDrawable
            drawable?.setColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }
    }
}