package com.example.registroduoc_solucion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.registroduoc_solucion.model.Catalogos
import com.example.registroduoc_solucion.ui.components.TarjetaEstudiante
import com.example.registroduoc_solucion.viewmodel.FormularioViewModel


// [63] PANTALLA 3 (NUEVA): todos los estudiantes registrados mientras la app está abierta.
@OptIn(ExperimentalMaterial3Api::class)   // TopAppBar todavía es "experimental" en Material 3
@Composable
fun ListaEstudiantesScreen(
    viewModel: FormularioViewModel,
    onVolver: () -> Unit,
    onNuevoRegistro: () -> Unit
) {
    val estudiantes by viewModel.estudiantes.collectAsState()

    // [64] Datos DERIVADOS: se calculan a partir de la lista cada vez que se dibuja; no se guardan aparte.
    val diurnos = estudiantes.count { it.jornada == Catalogos.JORNADAS.first() }
    val vespertinos = estudiantes.size - diurnos

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes registrados") },
                // [65] Flecha "atrás" de la barra superior. contentDescription es lo que lee TalkBack.
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Total: ${estudiantes.size} · Diurna: $diurnos · Vespertina: $vespertinos",
                style = MaterialTheme.typography.titleSmall
            )

            if (estudiantes.isEmpty()) {
                // [66] ESTADO VACÍO: siempre explicar por qué no se ve nada y qué se puede hacer.
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no hay estudiantes registrados.\nUsa el botón de abajo para agregar el primero.")
                }
            } else {
                // [67] LazyColumn dibuja SOLO las tarjetas que caben en pantalla: eficiente aunque haya cientos.
                LazyColumn(
                    modifier = Modifier.weight(1f),            // ocupa el espacio libre y deja lugar al botón
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    // key = id: le permite a Compose reconocer cada tarjeta aunque la lista cambie.
                    items(estudiantes, key = { it.id }) { estudiante ->
                        TarjetaEstudiante(estudiante)
                    }
                }
            }

            Button(
                onClick = onNuevoRegistro,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text("Registrar otro estudiante")
            }
        }
    }
}
