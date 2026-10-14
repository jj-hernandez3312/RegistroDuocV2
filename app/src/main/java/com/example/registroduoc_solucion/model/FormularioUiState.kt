package com.example.registroduoc_solucion.model

// [3] UI STATE: TODO lo que el formulario muestra en pantalla, reunido en UN solo objeto.
// Cada propiedad tiene un valor inicial, así el formulario parte vacío.
data class FormularioUiState(
    val nombre: String = "",                          // lo que el usuario escribió en "Nombre"
    val correo: String = "",                          // lo que el usuario escribió en "Correo"
    val aceptaTerminos: Boolean = false,              // ¿marcó la casilla de términos?
    val errores: FormularioErrores = FormularioErrores()  // [4] submodelo con los errores de cada campo
)