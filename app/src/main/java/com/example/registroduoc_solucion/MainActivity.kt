package com.example.registroduoc_solucion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.registroduoc_solucion.navigation.AppNavigation
import com.example.registroduoc_solucion.ui.screen.PerfilScreen
import com.example.registroduoc_solucion.ui.theme.PerfilDuocTheme
import com.example.registroduoc_solucion.viewmodel.PerfilViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // [41] Un solo ViewModel para el tema y para la pantalla.
            val viewModel: PerfilViewModel = viewModel()
            val estado by viewModel.estado.collectAsState()

            // [42] El tema depende de lo guardado en DataStore: al mover el Switch, TODA la app cambia de color.

            PerfilDuocTheme(darkTheme = estado.modoOscuro) {
                PerfilScreen(viewModel = viewModel)
            }
        }
    }
}