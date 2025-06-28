package com.cordova.gianella.innovacionneuroluxkids

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Clase de datos para representar el estado de la lámpara
data class LampState(
    val isOn: Boolean = true,
    val colorCode: Char = 'r',
    val colorName: String = "Rojo",
    val intensity: Int = 128
)

// ViewModel para compartir el estado de la lámpara entre actividades
class LampStateViewModel : ViewModel() {

    // _state es privado y mutable, solo el ViewModel puede cambiarlo.
    private val _state = MutableLiveData<LampState>()

    // state es público e inmutable, las vistas solo pueden observar los cambios.
    val state: LiveData<LampState> = _state

    init {
        // Establecer un estado inicial por defecto
        _state.value = LampState()
    }

    fun updateColor(newColorCode: Char) {
        val newState = _state.value?.copy(
            colorCode = newColorCode,
            colorName = getColorName(newColorCode)
        )
        _state.value = newState
    }

    fun updateIntensity(newIntensity: Int) {
        val newState = _state.value?.copy(intensity = newIntensity)
        _state.value = newState
    }

    fun updatePower(isOn: Boolean) {
        val newState = _state.value?.copy(isOn = isOn)
        _state.value = newState
    }

    private fun getColorName(code: Char): String {
        return when (code) {
            'r' -> "Rojo"
            'v' -> "Verde"
            'a' -> "Azul"
            'm' -> "Magenta"
            'c' -> "Cian"
            'n' -> "Naranja"
            else -> "Desconocido"
        }
    }
}