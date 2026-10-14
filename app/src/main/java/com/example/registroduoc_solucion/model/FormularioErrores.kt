package com.example.registroduoc_solucion.model

// [1] MODELO DE ERRORES: un "casillero" por cada campo que se valida.
// String? significa "puede haber un texto de error o puede ser null".
// null = ese campo NO tiene error.
data class FormularioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val aceptaTerminos: String? = null
) {
    // [2] Función de ayuda: devuelve true si AL MENOS UN campo tiene error.
    fun hayErrores(): Boolean {
        return nombre != null || correo != null || aceptaTerminos != null
    }
}