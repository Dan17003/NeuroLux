package com.cordova.gianella.innovacionneuroluxkids

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.material.switchmaterial.SwitchMaterial

class ControlLuzActivity : BaseActivity() {

    private lateinit var lampViewModel: LampStateViewModel
    private lateinit var colorButtons: Map<Int, Pair<Char, ImageView>>
    private lateinit var tvPowerLabel: TextView

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_control_luz
    }

    override fun getBottomNavigationMenuItemId(): Int {
        return R.id.nav_control
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lampViewModel = ViewModelProvider(this.applicationContext as ViewModelStoreOwner).get(LampStateViewModel::class.java)

        tvPowerLabel = findViewById(R.id.tvPowerLabel)

        lampViewModel.state.observe(this) { lampState ->
            updatePowerLabel(lampState.isOn)
        }

        setupColorPalette()
        setupIntensitySlider()
        setupOnOffSwitch()

        val initialColorCode = lampViewModel.state.value?.colorCode ?: 'r'
        val initialButtonId = colorButtons.entries.find { it.value.first == initialColorCode }?.key
        initialButtonId?.let {
            handleColorSelection(findViewById(it))
        }
    }

    private fun updatePowerLabel(isOn: Boolean) {
        if (isOn) {
            tvPowerLabel.text = "Encendido"
        } else {
            tvPowerLabel.text = "Apagado"
        }
    }

    private fun setupColorPalette() {
        colorButtons = mapOf(
            R.id.colorRed to ('r' to findViewById(R.id.checkRed)),
            R.id.colorGreen to ('v' to findViewById(R.id.checkGreen)),
            R.id.colorBlue to ('a' to findViewById(R.id.checkBlue)),
            R.id.colorMagenta to ('m' to findViewById(R.id.checkMagenta)),
            R.id.colorCyan to ('c' to findViewById(R.id.checkCyan)),
            R.id.colorOrange to ('n' to findViewById(R.id.checkOrange))
        )
        colorButtons.keys.forEach { buttonId ->
            findViewById<CardView>(buttonId).setOnClickListener {
                handleColorSelection(it)
            }
        }
    }

    private fun handleColorSelection(selectedView: View) {
        colorButtons.forEach { (id, pair) ->
            pair.second.visibility = if (id == selectedView.id) View.VISIBLE else View.GONE
        }
        val colorCode = colorButtons[selectedView.id]?.first ?: 'r'
        lampViewModel.updateColor(colorCode)
        publishMqttMessage(colorCode.toString())
    }

    private fun setupIntensitySlider() {
        val seekBar: SeekBar = findViewById(R.id.seekBarIntensity)
        val tvPercentage: TextView = findViewById(R.id.tvPercentage)

        seekBar.progress = lampViewModel.state.value?.intensity ?: 128
        val initialProgress = (seekBar.progress / 255.0 * 100).toInt()
        tvPercentage.text = "$initialProgress%"

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val percentage = (progress / 255.0 * 100).toInt()
                tvPercentage.text = "$percentage%"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val intensityValue = seekBar?.progress ?: 128
                lampViewModel.updateIntensity(intensityValue)
                publishMqttMessage("intensity:$intensityValue")
            }
        })
    }

    private fun setupOnOffSwitch() {
        val switchPower: SwitchMaterial = findViewById(R.id.switchPower)

        switchPower.isChecked = lampViewModel.state.value?.isOn ?: true

        switchPower.setOnCheckedChangeListener { _, isChecked ->
            lampViewModel.updatePower(isChecked)
            if (isChecked) {
                val lastIntensity = lampViewModel.state.value?.intensity ?: 128
                publishMqttMessage("intensity:$lastIntensity")
            } else {
                publishMqttMessage("intensity:0")
            }
        }
    }

    private fun publishMqttMessage(message: String) {
        val topic = "neuroluxkids/led/control"
        MqttClientManager.publish(topic, message)
    }
}