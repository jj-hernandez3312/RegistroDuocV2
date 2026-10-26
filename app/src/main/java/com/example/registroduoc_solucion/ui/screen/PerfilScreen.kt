package com.example.registroduoc_solucion.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.registroduoc_solucion.ui.components.BotonGuardar
import com.example.registroduoc_solucion.ui.components.FotoPerfil
import com.example.registroduoc_solucion.ui.components.MensajeGuardado
import com.example.registroduoc_solucion.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)   // TopAppBar todavía es experimental en Material 3
@Composable
fun PerfilScreen(viewModel: PerfilViewModel) {
    // [31] Observa el estado del ViewModel: cada cambio vuelve a dibujar (recompone) la pantalla.
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    // [32] rememberSaveable: estado que pertenece SOLO a esta pantalla y que sobrevive al girar el teléfono.
    // (Con remember a secas, el aviso de permiso desaparecería al girar).
    var permisoDenegado by rememberSaveable { mutableStateOf(false) }

    // [33] derivedStateOf: un valor CALCULADO a partir de otro estado.
    // Se vuelve a calcular cuando cambia el nombre y solo avisa un cambio si el RESULTADO cambia
    // (al escribir "Ana" → "Anab" las iniciales siguen siendo "A").
    val iniciales by remember {
        derivedStateOf { calcularIniciales(estado.nombre) }
    }

    // [34] LAUNCHER DE LA CÁMARA: abre la app de cámara del teléfono y recibe la foto (Bitmap),
    // o null si el usuario canceló.
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { foto ->
        if (foto != null) viewModel.onFotoTomada(foto)
    }

    // [35] LAUNCHER DEL PERMISO: muestra el diálogo del sistema «¿Permitir que Mi Perfil DUOC tome fotos?»
    // y responde true (Permitir) o false (No permitir).
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoDenegado = !concedido
        if (concedido) camaraLauncher.launch(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil DUOC") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FotoPerfil(foto = estado.foto, iniciales = iniciales)

            // [36] Al tocar: si YA hay permiso, abre la cámara; si no, primero lo pide.
            OutlinedButton(onClick = {
                val tienePermiso = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (tienePermiso) camaraLauncher.launch(null)
                else permisoLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Icon(Icons.Filled.Face, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (estado.foto == null) "Tomar foto" else "Cambiar foto")
            }

            // [37] Aviso animado: aparece solo si el usuario rechazó el permiso (manejo amigable del error).
            AnimatedVisibility(visible = permisoDenegado) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Sin permiso de cámara no podemos tomar tu foto. " +
                                    "Toca «Tomar foto» para intentarlo de nuevo.",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Campo de texto con validación (semana 9)
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = estado.errorNombre != null,
                supportingText = estado.errorNombre?.let { error -> { Text(error) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            BotonGuardar(guardando = estado.guardando, onClick = viewModel::guardarNombre)

            MensajeGuardado(visible = estado.mostrarGuardado)

            HorizontalDivider()

            // [38] Fila del Switch: toda la fila es tocable (toggleable) y el cambio se guarda AL INSTANTE.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = estado.modoOscuro,
                        onValueChange = viewModel::onModoOscuroChange,
                        role = Role.Switch
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Modo oscuro", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Se guarda al instante en el teléfono",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(checked = estado.modoOscuro, onCheckedChange = null)
            }

            // [39] Tarjeta con lo que está GUARDADO en DataStore: sobrevive a cerrar la app.
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Guardado en el teléfono", style = MaterialTheme.typography.titleMedium)
                    Text("Nombre: ${estado.nombreGuardado.ifBlank { "(sin guardar)" }}")
                    Text("Modo oscuro: ${if (estado.modoOscuro) "Sí" else "No"}")
                    Text("Veces que abriste la app: ${estado.visitas}")
                }
            }
        }
    }
}

// [40] Función auxiliar: "Ana María" → "AM", "ana" → "A", "" → "?".
private fun calcularIniciales(nombre: String): String {
    val palabras = nombre.trim().split(" ").filter { it.isNotBlank() }
    if (palabras.isEmpty()) return "?"
    return palabras.take(2).joinToString("") { it.first().uppercase() }
}