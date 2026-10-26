package com.example.registroduoc_solucion.viewmodel



import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.registroduoc_solucion.data.PreferenciasRepository
import com.example.registroduoc_solucion.model.PerfilUiState

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// [11] AndroidViewModel = un ViewModel que además recibe la Application.
// La necesitamos porque DataStore pide un Context para saber dónde guardar su archivo.
class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    // [12] El ViewModel usa el repositorio; no conoce los detalles de DataStore.
    private val repositorio = PreferenciasRepository(application)

    // [13] Estado privado (modificable) y estado público (solo lectura), igual que en la semana 9.
    private val _estado = MutableStateFlow(PerfilUiState())
    val estado: StateFlow<PerfilUiState> = _estado.asStateFlow()

    // [14] Sirve para copiar el nombre guardado al campo de texto SOLO la primera vez.
    private var nombreCargado = false

    // [15] init se ejecuta UNA vez, cuando se crea el ViewModel (al abrir la app; NO al girar el teléfono).
    init {
        // [16] Corrutina 1: suma una visita en DataStore.
        viewModelScope.launch {
            repositorio.sumarVisita()
        }
        // [17] Corrutina 2: escucha DataStore mientras exista el ViewModel. Cada vez que algo se guarda,
        // collect recibe los datos nuevos, el estado se actualiza y la pantalla se redibuja sola.
        viewModelScope.launch {
            repositorio.preferencias.collect { prefs ->
                _estado.update {
                    it.copy(
                        nombreGuardado = prefs.nombre,
                        modoOscuro = prefs.modoOscuro,
                        visitas = prefs.visitas,
                        nombre = if (nombreCargado) it.nombre else prefs.nombre
                    )
                }
                nombreCargado = true
            }
        }
    }

    // [18] EVENTO: el usuario escribe en el campo. Solo cambia la memoria (todavía NO se guarda).
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errorNombre = null) }
    }

    // [19] EVENTO: el usuario mueve el Switch. Se guarda DE INMEDIATO en DataStore.
    // Aquí no se toca _estado: el valor nuevo vuelve solo por el Flow de [17] (una sola fuente de verdad).
    fun onModoOscuroChange(activado: Boolean) {
        viewModelScope.launch {
            repositorio.guardarModoOscuro(activado)
        }
    }

    // [20] EVENTO: la cámara devolvió una foto. Se guarda SOLO en memoria (no en DataStore).
    fun onFotoTomada(foto: Bitmap) {
        _estado.update { it.copy(foto = foto) }
    }

    // [21] EVENTO: el usuario toca «Guardar nombre». Valida, muestra el loader, guarda y avisa.
    fun guardarNombre() {
        val nombre = _estado.value.nombre.trim()

        // [22] Validación (como en la semana 9): si hay error, se muestra y NO se guarda.
        val error = when {
            nombre.isBlank() -> "El nombre es obligatorio"
            nombre.length < 3 -> "El nombre debe tener al menos 3 letras"
            else -> null
        }
        if (error != null) {
            _estado.update { it.copy(errorNombre = error) }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(guardando = true) }        // [23] enciende el loader
            delay(800)                                           // pausa solo para que el loader se alcance a ver
            repositorio.guardarNombre(nombre)                    // [24] escribe en DataStore
            _estado.update { it.copy(guardando = false, mostrarGuardado = true) }  // [25] apaga el loader y muestra el mensaje
            delay(2500)
            _estado.update { it.copy(mostrarGuardado = false) }  // [26] el mensaje se va (con animación)
        }
    }
}