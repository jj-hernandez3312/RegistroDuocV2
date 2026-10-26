package com.example.registroduoc_solucion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

// [35] GRUPO DE RADIOBUTTON: de todas las jornadas, solo UNA puede estar elegida.
@Composable
fun SelectorJornada(
    seleccion: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit
) {
    Column {
        Text("Jornada", style = MaterialTheme.typography.labelLarge)

        // [36] selectableGroup() avisa al lector de pantalla (TalkBack) que estas opciones forman UN grupo.
        Row(modifier = Modifier.selectableGroup()) {
            opciones.forEach { opcion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    // [37] selectable hace que TODA la fila (círculo + texto) responda al toque, no solo el círculo.
                    modifier = Modifier
                        .selectable(
                            selected = seleccion == opcion,
                            onClick = { onSeleccion(opcion) },
                            role = Role.RadioButton
                        )
                        .padding(top = 8.dp, bottom = 8.dp, end = 24.dp)
                ) {
                    RadioButton(
                        selected = seleccion == opcion,   // marcado solo si es la opción elegida
                        onClick = null                    // null: el toque ya lo maneja la fila (selectable)
                    )
                    Text(opcion, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
