package com.example.registroduoc_solucion.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.registroduoc_solucion.model.Catalogos
import com.example.registroduoc_solucion.model.Estudiante
import com.example.registroduoc_solucion.model.FormularioErrores
import com.example.registroduoc_solucion.model.FormularioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// [13] VIEWMODEL (v2): ahora maneja DOS estados:
//   1) el formulario que se está llenando  → estado
//   2) la lista de estudiantes ya guardados → estudiantes
class FormularioViewModel : ViewModel() {

    // Estado del formulario (igual que en la v1): privado y modificable / público y de solo lectura.
    private val _estado = MutableStateFlow(FormularioUiState())
    val estado: StateFlow<FormularioUiState> = _estado.asStateFlow()

    // [14] NUEVO: lista de registrados. Parte vacía (emptyList()) y sigue el MISMO patrón privado/público.
    private val _estudiantes = MutableStateFlow<List<Estudiante>>(emptyList())
    val estudiantes: StateFlow<List<Estudiante>> = _estudiantes.asStateFlow()

    // [15] Contador para entregar ids únicos. Es "var" porque aumenta en cada registro y "private"
    // porque nadie de afuera debe tocarlo. No es StateFlow: ninguna pantalla lo dibuja.
    private var siguienteId = 1

    // ---------------- EVENTOS: la pantalla avisa, el ViewModel actualiza ----------------

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    // [16] FILTRO DE ENTRADA: antes de guardar se eliminan los caracteres que no son dígitos
    // y se dejan como máximo 2. Así es IMPOSIBLE que la edad contenga "abc" o "1234".
    fun onEdadChange(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(2)
        _estado.update { it.copy(edad = soloDigitos, errores = it.errores.copy(edad = null)) }
    }

    fun onCarreraChange(valor: String) {
        _estado.update { it.copy(carrera = valor, errores = it.errores.copy(carrera = null)) }
    }

    // [17] Jornada y semestre no borran ningún error porque NUNCA tienen error.
    fun onJornadaChange(valor: String) {
        _estado.update { it.copy(jornada = valor) }
    }

    fun onSemestreChange(valor: Int) {
        _estado.update { it.copy(semestre = valor) }
    }

    // [18] Al APAGAR las notificaciones se borran el teléfono y su error:
    // un dato que ya no se pide no debe quedar guardado "escondido".
    fun onNotificacionesChange(valor: Boolean) {
        _estado.update {
            it.copy(
                notificaciones = valor,
                telefono = if (valor) it.telefono else "",
                errores = it.errores.copy(telefono = null)
            )
        }
    }

    fun onTelefonoChange(valor: String) {
        val soloDigitos = valor.filter { it.isDigit() }.take(Catalogos.LARGO_TELEFONO)
        _estado.update { it.copy(telefono = soloDigitos, errores = it.errores.copy(telefono = null)) }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor, errores = it.errores.copy(aceptaTerminos = null)) }
    }

    // ---------------- VALIDACIÓN ----------------

    // [19] Revisa TODAS las reglas, guarda los errores en el estado y devuelve true si no hay ninguno.
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val correo = actual.correo.trim()
        val edad = actual.edad.toIntOrNull()        // [20] "19" → 19   |   "" → null

        val errores = FormularioErrores(
            nombre = when {
                actual.nombre.isBlank() -> "El nombre es obligatorio"
                actual.nombre.trim().length < 3 -> "El nombre debe tener al menos 3 letras"
                else -> null
            },
            // [21] Tres reglas en orden: obligatorio → formato válido → dominio institucional.
            correo = when {
                correo.isBlank() -> "El correo es obligatorio"
                !Patterns.EMAIL_ADDRESS.matcher(correo)
                    .matches() -> "El formato del correo no es válido"

                !correo.lowercase().endsWith(Catalogos.DOMINIO_INSTITUCIONAL) ->
                    "Usa tu correo institucional (${Catalogos.DOMINIO_INSTITUCIONAL})"

                else -> null
            },
            // [22] Regla de RANGO: "!in" pregunta si el número está FUERA del intervalo 17..99.
            edad = when {
                edad == null -> "La edad es obligatoria"
                edad !in Catalogos.EDAD_MINIMA..Catalogos.EDAD_MAXIMA ->
                    "La edad debe estar entre ${Catalogos.EDAD_MINIMA} y ${Catalogos.EDAD_MAXIMA} años"

                else -> null
            },
            carrera = if (actual.carrera.isBlank()) "Debes elegir una carrera" else null,
            // [23] Regla DEPENDIENTE: el teléfono solo se revisa si el Switch de notificaciones está encendido.
            telefono = when {
                !actual.notificaciones -> null
                actual.telefono.isBlank() -> "Ingresa un teléfono para recibir notificaciones"
                actual.telefono.length != Catalogos.LARGO_TELEFONO ->
                    "El teléfono debe tener ${Catalogos.LARGO_TELEFONO} dígitos"

                else -> null
            },
            aceptaTerminos = if (!actual.aceptaTerminos) "Debes aceptar los términos" else null
        )

        _estado.update { it.copy(errores = errores) }
        return !errores.hayErrores()
    }

    // ---------------- GUARDAR ----------------

    // [24] Convierte el formulario (UiState) en un Estudiante, lo agrega a la lista, limpia el formulario
    // y devuelve el id (la navegación lo necesita para armar la ruta "resumen/{id}").
    // Devuelve Int? (puede ser null): si alguien la llama con datos inválidos, NO guarda nada.
    fun registrarEstudiante(): Int? {
        if (!validarFormulario()) return null       // [25] defensa: nunca se guarda un registro inválido

        val actual = _estado.value
        val nuevo = Estudiante(
            id = siguienteId,
            nombre = actual.nombre.trim(),
            correo = actual.correo.trim().lowercase(),
            edad = actual.edad.toInt(),              // seguro: la validación ya comprobó que es un número
            carrera = actual.carrera,
            jornada = actual.jornada,
            semestre = actual.semestre,
            notificaciones = actual.notificaciones,
            telefono = actual.telefono
        )
        siguienteId++
        _estudiantes.update { lista -> lista + nuevo }   // [26] lista + elemento = una lista NUEVA con uno más
        limpiarFormulario()
        return nuevo.id
    }

    // Vuelve el formulario a su estado inicial (todo vacío y sin errores). La lista NO se toca.
    fun limpiarFormulario() {
        _estado.value = FormularioUiState()
    }
}
