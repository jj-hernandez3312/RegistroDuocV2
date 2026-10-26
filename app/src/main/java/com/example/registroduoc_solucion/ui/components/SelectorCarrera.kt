package com.example.registroduoc_solucion.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

// [30] LISTA DESPLEGABLE (dropdown): un campo que, al tocarlo, abre un menú con las carreras.
// ExposedDropdownMenuBox todavía es "experimental" en Material 3: @OptIn indica que lo usamos sabiéndolo.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCarrera(
    seleccion: String,
    opciones: List<String>,
    error: String?,
    onSeleccion: (String) -> Unit
) {
    // [31] ¿Está abierto el menú? Es estado PURAMENTE VISUAL de este componente:
    // al ViewModel no le importa si el menú está abierto, solo qué carrera se eligió.
    var expandido by remember { mutableStateOf(false) }

    // [32] ExposedDropdownMenuBox coordina el campo y el menú: abre, cierra y ubica el menú bajo el campo.
    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it }
    ) {
        OutlinedTextField(
            value = seleccion,
            onValueChange = {},              // no se escribe: solo se elige del menú
            readOnly = true,                 // el teclado NO aparece
            label = { Text("Carrera") },
            placeholder = { Text("Selecciona tu carrera") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },  // flecha ▼ / ▲
            isError = error != null,
            supportingText = {
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)  // [33] "ancla": el menú se abre aquí
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }   // tocar fuera del menú lo cierra
        ) {
            // [34] Un DropdownMenuItem por cada carrera del catálogo.
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)   // 1) avisa al ViewModel qué carrera se eligió
                        expandido = false     // 2) cierra el menú
                    }
                )
            }
        }
    }
}
