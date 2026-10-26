package com.example.registroduoc_solucion.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.registroduoc_solucion.model.Catalogos
import com.example.registroduoc_solucion.ui.components.CampoTexto
import com.example.registroduoc_solucion.ui.components.DialogoConfirmacion
import com.example.registroduoc_solucion.ui.components.SelectorCarrera
import com.example.registroduoc_solucion.ui.components.SelectorJornada
import com.example.registroduoc_solucion.ui.components.SelectorSemestre
import com.example.registroduoc_solucion.viewmodel.FormularioViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// [46] PANTALLA 1 (v2): el formulario completo. Recibe el ViewModel y DOS acciones de navegación.
@OptIn(ExperimentalMaterial3Api::class)   // TopAppBar todavía es "experimental" en Material 3
@Composable
fun FormularioScreen(
    viewModel: FormularioViewModel,
    onRegistrado: (Int) -> Unit,        // [47] recibe el id del estudiante recién guardado
    onVerRegistrados: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val estudiantes by viewModel.estudiantes.collectAsState()   // solo para mostrar el contador de la barra

    // [48] Herramientas del Snackbar: el "control remoto" (SnackbarHostState) y un scope para lanzar corrutinas.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // [49] ¿Se ve el diálogo de confirmación? Se usa rememberSaveable (y no remember)
    // para que el diálogo SIGA abierto si el usuario gira el teléfono.
    var mostrarConfirmacion by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        // [50] Barra superior: título y un botón de texto con la cantidad de registrados.
        topBar = {
            TopAppBar(
                title = { Text("Registro DUOC") },
                actions = {
                    TextButton(onClick = onVerRegistrados) {
                        Text("Registrados (${estudiantes.size})")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }   // dónde aparecen los Snackbar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // [51] BARRA DE PROGRESO en tiempo real: avanza a medida que se completan los campos obligatorios.
            LinearProgressIndicator(
                progress = { estado.progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Text(
                "Completado: ${(estado.progreso * 100).roundToInt()} %",
                style = MaterialTheme.typography.bodySmall
            )

            // ---------------- 1. Datos personales ----------------
            TituloSeccion("1. Datos personales")

            CampoTexto(
                valor = estado.nombre,
                error = estado.errores.nombre,
                label = "Nombre completo",
                onChange = { nuevo -> viewModel.onNombreChange(nuevo) }
            )
            CampoTexto(
                valor = estado.correo,
                error = estado.errores.correo,
                label = "Correo institucional",
                onChange = { nuevo -> viewModel.onCorreoChange(nuevo) },
                tipoTeclado = KeyboardType.Email,
                ayuda = "Ej: nombre.apellido${Catalogos.DOMINIO_INSTITUCIONAL}"
            )
            CampoTexto(
                valor = estado.edad,
                error = estado.errores.edad,
                label = "Edad",
                onChange = { nuevo -> viewModel.onEdadChange(nuevo) },
                tipoTeclado = KeyboardType.Number,             // teclado solo con números
                ayuda = "Entre ${Catalogos.EDAD_MINIMA} y ${Catalogos.EDAD_MAXIMA} años"
            )

            // ---------------- 2. Datos académicos ----------------
            TituloSeccion("2. Datos académicos")

            SelectorCarrera(
                seleccion = estado.carrera,
                opciones = Catalogos.CARRERAS,
                error = estado.errores.carrera,
                onSeleccion = { carrera -> viewModel.onCarreraChange(carrera) }
            )
            SelectorJornada(
                seleccion = estado.jornada,
                opciones = Catalogos.JORNADAS,
                onSeleccion = { jornada -> viewModel.onJornadaChange(jornada) }
            )
            SelectorSemestre(
                semestre = estado.semestre,
                onCambio = { semestre -> viewModel.onSemestreChange(semestre) }
            )

            // ---------------- 3. Preferencias ----------------
            TituloSeccion("3. Preferencias")

            // [52] SWITCH: actúa de inmediato → al encenderlo aparece el campo teléfono en ese mismo instante.
            // toggleable hace que TODA la fila (texto + interruptor) responda al toque, igual que en SelectorJornada.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = estado.notificaciones,
                        onValueChange = { activado -> viewModel.onNotificacionesChange(activado) },
                        role = Role.Switch
                    )
                    .padding(vertical = 4.dp)
            ) {
                Text("Recibir notificaciones por SMS", modifier = Modifier.weight(1f))
                Switch(
                    checked = estado.notificaciones,
                    onCheckedChange = null          // null: el toque lo maneja la fila (toggleable)
                )
            }

            // [53] CAMPO CONDICIONAL: el teléfono solo EXISTE en pantalla si el Switch está encendido.
            if (estado.notificaciones) {
                CampoTexto(
                    valor = estado.telefono,
                    error = estado.errores.telefono,
                    label = "Teléfono móvil",
                    onChange = { nuevo -> viewModel.onTelefonoChange(nuevo) },
                    tipoTeclado = KeyboardType.Phone,
                    ayuda = "${Catalogos.LARGO_TELEFONO} dígitos, ej: 912345678",
                    prefijo = "+56 "
                )
            }

            // Misma técnica para el Checkbox: se puede tocar la casilla o el texto.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = estado.aceptaTerminos,
                        onValueChange = { marcado -> viewModel.onAceptaTerminosChange(marcado) },
                        role = Role.Checkbox
                    )
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = null
                )
                Text("Acepto los términos y condiciones", modifier = Modifier.padding(start = 8.dp))
            }
            estado.errores.aceptaTerminos?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // [54] Botón Enviar: DESHABILITADO (gris) mientras no se acepten los términos (estado.puedeEnviar).
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        mostrarConfirmacion = true          // todo bien → pedir confirmación
                    } else {
                        // [55] Hay errores → Snackbar con la cantidad. Se lee viewModel.estado.value (el valor
                        // recién calculado) y no "estado", que se actualizará recién en el próximo dibujo.
                        val cantidad = viewModel.estado.value.errores.cantidad()
                        val mensaje = if (cantidad == 1) {
                            "Revisa 1 campo marcado en rojo"
                        } else {
                            "Revisa $cantidad campos marcados en rojo"
                        }
                        scope.launch { snackbarHostState.showSnackbar(mensaje) }
                    }
                },
                enabled = estado.puedeEnviar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // [56] VISTA PREVIA EN TIEMPO REAL: lee el MISMO estado que los campos (fuente única de verdad).
            Text("Vista previa en tiempo real", style = MaterialTheme.typography.titleMedium)
            Text("Nombre: ${estado.nombre}")
            Text("Correo: ${estado.correo}")
            Text("Edad: ${estado.edad}")
            Text("Carrera: ${estado.carrera.ifBlank { "(sin elegir)" }}")
            Text("Jornada: ${estado.jornada} · Semestre: ${estado.semestre}")
            Text("Notificaciones: ${if (estado.notificaciones) "Sí, al +56 ${estado.telefono}" else "No"}")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // [57] DIÁLOGO: se dibuja SOLO mientras mostrarConfirmacion sea true. Va fuera del Scaffold
    // porque un diálogo flota sobre toda la pantalla, no ocupa un lugar dentro de la columna.
    if (mostrarConfirmacion) {
        DialogoConfirmacion(
            titulo = "¿Confirmas el registro?",
            mensaje = "Se registrará a ${estado.nombre.trim()} en ${estado.carrera}, " +
                    "jornada ${estado.jornada.lowercase()}, semestre ${estado.semestre}.",
            onConfirmar = {
                mostrarConfirmacion = false
                val id = viewModel.registrarEstudiante()   // guarda y devuelve el id (o null si algo falló)
                if (id != null) {
                    onRegistrado(id)                        // navega a "resumen/{id}"
                }
            },
            onCancelar = { mostrarConfirmacion = false }
        )
    }
}

// [58] Título de cada sección del formulario. "private" porque solo se usa en este archivo.
@Composable
private fun TituloSeccion(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}
