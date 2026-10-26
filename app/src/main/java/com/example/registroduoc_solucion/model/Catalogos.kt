package com.example.registroduoc_solucion.model

// [1] CATÁLOGOS: las opciones FIJAS del formulario y los límites de las reglas, escritos en UN solo lugar.
// "object" crea un objeto único (no se crea con paréntesis): se usa directamente → Catalogos.CARRERAS
object Catalogos {

    // [2] Opciones de la lista desplegable de carreras y de los RadioButton de jornada.
    // listOf(...) crea una lista de solo lectura: nadie puede agregar ni quitar opciones por accidente.
    val CARRERAS = listOf(
        "Analista Programador",
        "Ingeniería en Informática",
        "Ing. en Informática mención Desarrollo de Software",
        "Ing. en Informática mención Ciencia de Datos",
        "Ing. en Informática mención Inteligencia Artificial"
    )

    val JORNADAS = listOf("Diurna", "Vespertina")   // la primera es la que viene marcada al inicio

    // [3] LÍMITES de las reglas de validación. "const val" = constante que se conoce al compilar.
    // Si mañana la edad mínima cambia, se modifica SOLO aquí y todo el proyecto se entera.
    const val EDAD_MINIMA = 17
    const val EDAD_MAXIMA = 99
    const val SEMESTRE_MINIMO = 1
    const val SEMESTRE_MAXIMO = 10
    const val LARGO_TELEFONO = 9
    const val DOMINIO_INSTITUCIONAL = "@duocuc.cl"
}
