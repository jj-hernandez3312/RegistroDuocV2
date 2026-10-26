package com.example.registroduoc_solucion.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Botón que se convierte en "cargando" mientras se guarda.
@Composable
fun BotonGuardar(
    guardando: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !guardando,   // deshabilitado mientras guarda: evita guardar dos veces
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        // [29] LOADER: mientras guarda, aparece un indicador circular que gira junto al texto.
        if (guardando) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.5.dp,
                color = LocalContentColor.current
            )
            Spacer(Modifier.width(12.dp))
            Text("Guardando…")
        } else {
            Text("Guardar nombre")
        }
    }
}