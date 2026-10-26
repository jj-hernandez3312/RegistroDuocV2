package com.example.registroduoc_solucion.model

// [1] MODELO DE ERRORES: un "casillero" por cada campo que se valida.
// String? significa "puede haber un texto de error o puede ser null".
// null = ese campo NO tiene error.
data class FormularioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val edad: String? = null,            // NUEVO v2
    val carrera: String? = null,         // NUEVO v2
    val telefono: String? = null,        // NUEVO v2 (solo se revisa si activó las notificaciones)
    val aceptaTerminos: String? = null
) {
    // [7] Cuenta cuántos campos tienen error: listOf(...) junta los 6 casilleros en una lista
    // y count { it != null } cuenta los que tienen mensaje. Lo usa el Snackbar ("Revisa 3 campos…").
    fun cantidad(): Int {
        return listOf(nombre, correo, edad, carrera, telefono, aceptaTerminos).count { it != null }
    }

    // [8] Reutiliza cantidad(): si hay 1 o más errores, el formulario NO es válido.
    fun hayErrores(): Boolean {
        return cantidad() > 0
    }
}
