package com.example.registroduoc_solucion.model


// [9] UI STATE (v2): TODO lo que el formulario muestra, reunido en UN solo objeto.
// Cada propiedad tiene un valor inicial, así FormularioUiState() representa el formulario vacío.
data class FormularioUiState(
    // --- 1. Datos personales ---
    val nombre: String = "",
    val correo: String = "",
    val edad: String = "",                              // [10] String y no Int: guarda lo que se ESCRIBE ("", "1", "19")
    // --- 2. Datos académicos ---
    val carrera: String = "",                           // "" = todavía no elige ninguna
    val jornada: String = Catalogos.JORNADAS.first(),   // "Diurna" marcada al inicio
    val semestre: Int = Catalogos.SEMESTRE_MINIMO,      // el Slider parte en 1
    // --- 3. Preferencias ---
    val notificaciones: Boolean = false,                // Switch apagado al inicio
    val telefono: String = "",                          // solo se pide si notificaciones = true
    val aceptaTerminos: Boolean = false,
    // --- Errores de cada campo ---
    val errores: FormularioErrores = FormularioErrores()
) {
    // [11] PROPIEDAD CALCULADA: no se guarda; se calcula cada vez que alguien la lee (get()).
    // La regla "¿se puede presionar Enviar?" vive en el MODELO, no en la pantalla.
    val puedeEnviar: Boolean
        get() = aceptaTerminos

    // [12] PROGRESO: fracción de campos obligatorios que ya tienen algo (0.0 = nada, 1.0 = todo).
    // La usa la barra de progreso, que avanza en tiempo real mientras el usuario completa el formulario.
    val progreso: Float
        get() {
            val completos = listOf(
                nombre.isNotBlank(),
                correo.isNotBlank(),
                edad.isNotBlank(),
                carrera.isNotBlank(),
                aceptaTerminos
            )
            return completos.count { it } / completos.size.toFloat()
        }
}
