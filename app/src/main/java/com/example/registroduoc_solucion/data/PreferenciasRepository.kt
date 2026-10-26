package com.example.registroduoc_solucion.data



import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.example.registroduoc_solucion.model.PreferenciasUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// [5] Crea UN SOLO DataStore para toda la app, guardado en el archivo "preferencias_perfil".
// "by preferencesDataStore" asegura que exista una única instancia (dos instancias del mismo archivo hacen fallar la app).
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_perfil")

// [6] REPOSITORIO: la única clase que sabe CÓMO y DÓNDE se guardan los datos.
// El ViewModel le dice "guarda esto" o "dame esto" sin saber que existe DataStore.
class PreferenciasRepository(private val context: Context) {

    // [7] CLAVES: el "nombre del cajón" donde se guarda cada dato, junto con su TIPO.
    private object Claves {
        val NOMBRE = stringPreferencesKey("nombre")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val VISITAS = intPreferencesKey("visitas")
    }

    // [8] LEER: un Flow que entrega las preferencias AHORA y otra vez CADA VEZ que cambian.
    // map convierte el contenido de DataStore (Preferences) en nuestro modelo PreferenciasUsuario.
    // ?: entrega un valor por defecto cuando todavía no hay nada guardado (la primera vez).
    val preferencias: Flow<PreferenciasUsuario> = context.dataStore.data.map { prefs ->
        PreferenciasUsuario(
            nombre = prefs[Claves.NOMBRE] ?: "",
            modoOscuro = prefs[Claves.MODO_OSCURO] ?: false,
            visitas = prefs[Claves.VISITAS] ?: 0
        )
    }

    // [9] ESCRIBIR: edit { } guarda el cambio en el archivo de forma segura (se guarda todo o nada).
    // suspend = la función puede tardar; se ejecuta en una corrutina y no congela la pantalla.
    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { prefs -> prefs[Claves.NOMBRE] = nombre }
    }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { prefs -> prefs[Claves.MODO_OSCURO] = activado }
    }

    // [10] Lee el valor actual y le suma 1 dentro de la MISMA edición.
    suspend fun sumarVisita() {
        context.dataStore.edit { prefs ->
            val actual = prefs[Claves.VISITAS] ?: 0
            prefs[Claves.VISITAS] = actual + 1
        }
    }
}