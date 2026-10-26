package com.example.registroduoc_solucion.model

// [4] ESTUDIANTE: un registro YA VALIDADO y guardado en la lista.
// Se diferencia de FormularioUiState en dos cosas:
//   - cada dato tiene su tipo "real" (edad es Int, no String), porque ya pasó la validación;
//   - no tiene errores: un estudiante guardado nunca está "a medio llenar".
data class Estudiante(
    val id: Int,                 // [5] número único (1, 2, 3...). Es el dato que viaja en la ruta "resumen/{id}"
    val nombre: String,
    val correo: String,
    val edad: Int,
    val carrera: String,
    val jornada: String,
    val semestre: Int,
    val notificaciones: Boolean,
    val telefono: String         // "" si el estudiante no activó las notificaciones
)
