package com.example.registroduoc_solucion.navigation

import androidx.compose.runtime.Composable
import com.example.registroduoc_solucion.ui.screen.FormularioScreen
import com.example.registroduoc_solucion.ui.screen.ListaEstudiantesScreen
import com.example.registroduoc_solucion.ui.screen.ResumenScreen
import com.example.registroduoc_solucion.viewmodel.FormularioViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// [68] RUTAS (v2): ahora hay 3 pantallas, y una de ellas recibe un ARGUMENTO.
object Rutas {
    const val FORMULARIO = "formulario"
    const val LISTA = "lista"

    // [69] Ruta CON argumento: {id} es un "hueco" que se rellena al navegar.
    const val ARG_ID = "id"
    const val RESUMEN = "resumen/{id}"
    fun resumen(id: Int): String = "resumen/$id"     // Rutas.resumen(3) → "resumen/3"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // [70] Igual que en la v1: UN ViewModel compartido, creado FUERA del NavHost y entregado a las 3 pantallas.
    val viewModel: FormularioViewModel = viewModel()

    NavHost(navController = navController, startDestination = Rutas.FORMULARIO) {

        composable(Rutas.FORMULARIO) {
            FormularioScreen(
                viewModel = viewModel,
                // [71] Al registrar se navega a la ruta con el hueco ya relleno: "resumen/3".
                onRegistrado = { id -> navController.navigate(Rutas.resumen(id)) },
                onVerRegistrados = { navController.navigate(Rutas.LISTA) }
            )
        }

        // [72] Destino con argumento: se declara su NOMBRE ("id") y su TIPO (IntType) para recibirlo como número.
        composable(
            route = Rutas.RESUMEN,
            arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.IntType })
        ) { entrada ->
            // [73] Se lee el argumento desde la "entrada" de la pila. "?: 0" = valor de reserva si faltara.
            val id = entrada.arguments?.getInt(Rutas.ARG_ID) ?: 0
            ResumenScreen(
                viewModel = viewModel,
                estudianteId = id,
                onRegistrarOtro = { navController.popBackStack() },   // vuelve al formulario (ya vacío)
                onVerTodos = { navController.navigate(Rutas.LISTA) }
            )
        }

        composable(Rutas.LISTA) {
            ListaEstudiantesScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() },
                // [74] popBackStack(ruta, inclusive = false): saca pantallas de la pila HASTA llegar al
                // formulario, sin sacarlo a él. Así no se acumulan pantallas repetidas en la pila.
                onNuevoRegistro = { navController.popBackStack(Rutas.FORMULARIO, inclusive = false) }
            )
        }
    }
}
