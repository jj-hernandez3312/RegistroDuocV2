package com.example.registroduoc_solucion.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

// Componente reutilizable: muestra la foto de perfil o, si no hay foto, las iniciales.
@Composable
fun FotoPerfil(
    foto: Bitmap?,
    iniciales: String,
    modifier: Modifier = Modifier
) {
    // [27] ANIMACIÓN DE VALOR: cuando llega una foto, el borde pasa de gris a color primario y se engrosa.
    // animate*AsState no cambia de golpe: calcula los valores intermedios durante 600 ms.
    val colorBorde by animateColorAsState(
        targetValue = if (foto != null) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(durationMillis = 600),
        label = "colorBorde"
    )
    val grosorBorde by animateDpAsState(
        targetValue = if (foto != null) 5.dp else 2.dp,
        animationSpec = tween(durationMillis = 600),
        label = "grosorBorde"
    )

    Box(
        modifier = modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .border(grosorBorde, colorBorde, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (foto != null) {
            // [28] Bitmap (formato de Android) → ImageBitmap (formato que entiende Compose).
            Image(
                bitmap = foto.asImageBitmap(),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = iniciales,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}