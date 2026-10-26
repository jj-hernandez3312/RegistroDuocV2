package com.example.registroduoc_solucion.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

// [14] COMPONENTE REUTILIZABLE: un campo de texto que ya sabe mostrar su error.
// No guarda nada: recibe el valor y AVISA los cambios con onChange (state hoisting).
// [27] COMPONENTE REUTILIZABLE (v2): el mismo campo de la v1, ahora con texto de AYUDA y PREFIJO.
// Sigue sin guardar nada: recibe el valor y AVISA los cambios con onChange (state hoisting).
@Composable
fun CampoTexto(
    valor: String,
    error: String?,
    label: String,
    onChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    ayuda: String? = null,       // NUEVO v2: texto gris bajo el campo cuando NO hay error
    prefijo: String? = null      // NUEVO v2: texto fijo antes de lo escrito (ej: "+56 ")
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        // [28] Bajo el campo: si hay error se muestra el error (rojo); si no, la ayuda (gris).
        supportingText = {
            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
            } else if (ayuda != null) {
                Text(ayuda)
            }
        },
        // [29] prefix espera "una función que dibuja" o null. Si no hay prefijo, se entrega null y no se dibuja nada.
        prefix = if (prefijo != null) {
            { Text(prefijo) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = Modifier.fillMaxWidth()
    )
}
