package com.example.ashborn.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Estensione per creare un DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class DataStoreManager private constructor(private val context: Context) {

    // Chiavi per le preferenze
    private val USERNAME_KEY = stringPreferencesKey("username_key")
    private val COGNOME_KEY = stringPreferencesKey("cognome_key")
    private val COD_CLIENTE_KEY = stringPreferencesKey("cod_cliente_key")

    // Flow per la lettura delle preferenze
    val usernameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERNAME_KEY] ?: ""
    }
    val cognomeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[COGNOME_KEY] ?: ""
    }
    val codClienteFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[COD_CLIENTE_KEY] ?: ""
    }

    // Funzioni per scrivere le preferenze
    suspend fun writeUsername(userName: String) {
        context.dataStore.edit { settings ->
            settings[USERNAME_KEY] = userName
        }
    }

    suspend fun writeCognome(cognome: String) {
        context.dataStore.edit { settings ->
            settings[COGNOME_KEY] = cognome
        }
    }

    suspend fun writeCodCliente(codCliente: String) {
        context.dataStore.edit { settings ->
            settings[COD_CLIENTE_KEY] = codCliente
        }
    }

    suspend fun writeUserPreferences(user: User) {
        writeCognome(user.surname)
        writeUsername(user.name)
        writeCodCliente(user.clientCode)
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoreManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}

data class User(val name: String, val surname: String, val clientCode: String)