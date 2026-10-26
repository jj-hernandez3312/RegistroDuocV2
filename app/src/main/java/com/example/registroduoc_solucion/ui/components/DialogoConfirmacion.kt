package com.example.registroduoc_solucion.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

// [41] DIÁLOGO DE CONFIRMACIÓN reutilizable. NO decide cuándo mostrarse: eso lo decide quien lo usa,
// envolviéndolo en un if (mostrar) { DialogoConfirmacion(...) }.
@Composable
fun DialogoConfirmacion(
    titulo: String,
    mensaje: String,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,          // tocar fuera del diálogo o "Atrás" = cancelar
        title = { Text(titulo) },
        text = { Text(mensaje) },
        // [42] confirmButton = acción principal (a la derecha); dismissButton = la que cancela (a su izquierda).
        confirmButton = {
            TextButton(onClick = onConfirmar) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}
