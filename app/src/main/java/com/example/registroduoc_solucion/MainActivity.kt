package com.example.registroduoc_solucion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.registroduoc_solucion.navigation.AppNavigation
import com.example.registroduoc_solucion.ui.theme.RegistroDuoc_SolucionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroDuoc_SolucionTheme() {
                AppNavigation()   // [35] desde aquí, AppNavigation decide qué pantalla se ve
            }
        }
    }
}