package com.example.registroduoc_solucion.model



import android.graphics.Bitmap

// [4] ESTADO DE LA PANTALLA: todo lo que la pantalla necesita para dibujarse.
// Mezcla datos que vienen de DataStore (guardados) con datos que solo viven en memoria.
data class PerfilUiState(
    val nombre: String = "",              // lo que el usuario está escribiendo (borrador, aún no guardado)
    val errorNombre: String? = null,      // mensaje de error del campo (null = sin error)
    val nombreGuardado: String = "",      // lo que está guardado en DataStore
    val modoOscuro: Boolean = false,      // tema elegido (viene de DataStore)
    val visitas: Int = 0,                 // veces que se abrió la app (viene de DataStore)
    val foto: Bitmap? = null,             // foto tomada con la cámara (SOLO en memoria)
    val guardando: Boolean = false,       // true mientras se guarda → se muestra el loader
    val mostrarGuardado: Boolean = false  // true → aparece el mensaje animado «¡Guardado con éxito!»
)