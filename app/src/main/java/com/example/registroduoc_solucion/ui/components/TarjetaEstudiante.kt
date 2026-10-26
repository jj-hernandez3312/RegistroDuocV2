package com.example.registroduoc_solucion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.registroduoc_solucion.model.Estudiante


// [43] TARJETA de un estudiante registrado. La usan DOS pantallas (Resumen y Lista),
// por eso vive en components y recibe un Estudiante ya listo para mostrar.
// "modifier: Modifier = Modifier" es la convención de Compose para que quien la usa pueda ajustar su tamaño o margen.
@Composable
fun TarjetaEstudiante(estudiante: Estudiante, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("#${estudiante.id} · ${estudiante.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(estudiante.correo, color = MaterialTheme.colorScheme.primary)
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            DatoFila("Edad", "${estudiante.edad} años")
            DatoFila("Carrera", estudiante.carrera)
            DatoFila("Jornada", estudiante.jornada)
            DatoFila("Semestre", "${estudiante.semestre}°")
            // [44] if como EXPRESIÓN: el texto depende de si el estudiante activó las notificaciones.
            DatoFila(
                "Notificaciones",
                if (estudiante.notificaciones) "Sí, al +56 ${estudiante.telefono}" else "No"
            )
        }
    }
}

// [45] "private": esta función pequeña solo se usa dentro de este archivo. Evita repetir 5 veces el mismo Row.
@Composable
private fun DatoFila(etiqueta: String, valor: String) {
    Row {
        Text("$etiqueta: ", fontWeight = FontWeight.SemiBold)
        Text(valor)
    }
}
