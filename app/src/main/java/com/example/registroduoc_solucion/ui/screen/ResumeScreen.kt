package com.example.registroduoc_solucion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.registroduoc_solucion.ui.components.TarjetaEstudiante
import com.example.registroduoc_solucion.viewmodel.FormularioViewModel


// [59] PANTALLA 2 (v2): ya NO lee el formulario (se limpió al registrar). Recibe un id por la RUTA
// y busca a ese estudiante en la lista del ViewModel.
@Composable
fun ResumenScreen(
    viewModel: FormularioViewModel,
    estudianteId: Int,
    onRegistrarOtro: () -> Unit,
    onVerTodos: () -> Unit
) {
    val estudiantes by viewModel.estudiantes.collectAsState()

    // [60] find { } devuelve el PRIMER elemento que cumple la condición, o null si no hay ninguno.
    val estudiante = estudiantes.find { it.id == estudianteId }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // [61] Si el id no existe se muestra un aviso, en vez de cerrar la app con un error.
            if (estudiante == null) {
                Text("No se encontró el estudiante #$estudianteId", style = MaterialTheme.typography.titleMedium)
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,                 // decorativo: el texto de abajo ya lo dice
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
                Text("¡Registro exitoso!", style = MaterialTheme.typography.headlineSmall)
                Text("El estudiante #${estudiante.id} quedó guardado.")
                TarjetaEstudiante(estudiante)
            }

            // [62] Jerarquía de botones: UNA acción principal (Button) y una secundaria (OutlinedButton).
            Button(onClick = onRegistrarOtro, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar otro estudiante")
            }
            OutlinedButton(onClick = onVerTodos, modifier = Modifier.fillMaxWidth()) {
                Text("Ver todos los registrados (${estudiantes.size})")
            }
        }
    }
}
