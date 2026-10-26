package com.example.registroduoc_solucion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.registroduoc_solucion.model.Catalogos

import kotlin.math.roundToInt

// [38] SLIDER: elegir el semestre arrastrando. El Slider trabaja con Float; el ViewModel guarda Int.
@Composable
fun SelectorSemestre(
    semestre: Int,
    onCambio: (Int) -> Unit
) {
    Column {
        // Este texto cambia en tiempo real mientras se arrastra el Slider.
        Text("Semestre que cursas: $semestre", style = MaterialTheme.typography.labelLarge)

        Slider(
            value = semestre.toFloat(),                       // [39] Int → Float para el Slider
            onValueChange = { onCambio(it.roundToInt()) },    //      Float → Int (redondeado) para el ViewModel
            valueRange = Catalogos.SEMESTRE_MINIMO.toFloat()..Catalogos.SEMESTRE_MAXIMO.toFloat(),
            // [40] steps = marcas INTERMEDIAS. De 1 a 10 hay 10 valores → 8 marcas entre los dos extremos.
            steps = Catalogos.SEMESTRE_MAXIMO - Catalogos.SEMESTRE_MINIMO - 1
        )
    }
}
