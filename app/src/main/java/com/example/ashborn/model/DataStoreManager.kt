package com.example.ashborn.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Estensione per creare un DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class DataStoreManager private constructor(private val context: Context) {



    // Chiavi per le preferenze
    private val HAS_REQUESTED_PERMISSION_KEY = booleanPreferencesKey("has_requested_permission")
    private val USERNAME_KEY = stringPreferencesKey("username_key")
    private val COGNOME_KEY = stringPreferencesKey("cognome_key")
    private val COD_CLIENTE_KEY = stringPreferencesKey("cod_cliente_key")
    private val WRONG_ATTEMPTS_KEY = longPreferencesKey("wrong_attempts_key")
    private val TIMER_KEY = longPreferencesKey("timer_key")

    // Flow per la lettura delle preferenze
    var timerFlow: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[TIMER_KEY] ?: 0L
    }
    var wrongAttemptsFlow: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[WRONG_ATTEMPTS_KEY] ?: 0L
    }
    var usernameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERNAME_KEY] ?: ""
    }
    var cognomeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[COGNOME_KEY] ?: ""
    }
    var codClienteFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[COD_CLIENTE_KEY] ?: ""
    }
    var hasRequestedPermissionFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAS_REQUESTED_PERMISSION_KEY] ?: false
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

    suspend fun writeTimer(timeSeconds: Long) {
        context.dataStore.edit { settings ->
            settings[TIMER_KEY] = timeSeconds
        }
    }

    suspend fun writeWrongAttempts(wrongAttempts: Long) {
        context.dataStore.edit { settings ->
            settings[WRONG_ATTEMPTS_KEY] = wrongAttempts
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

    suspend fun writeHasRequestedPermission(hasRequestedPermission: Boolean) {
        context.dataStore.edit { settings ->
            settings[HAS_REQUESTED_PERMISSION_KEY] = hasRequestedPermission
        }
    }

    fun reload() {
        timerFlow = context.dataStore.data.map { preferences ->
            preferences[TIMER_KEY] ?: 0L
        }
        wrongAttemptsFlow = context.dataStore.data.map { preferences ->
            preferences[WRONG_ATTEMPTS_KEY] ?: 0L
        }
        usernameFlow = context.dataStore.data.map { preferences ->
            preferences[USERNAME_KEY] ?: ""
        }
        cognomeFlow = context.dataStore.data.map { preferences ->
            preferences[COGNOME_KEY] ?: ""
        }
        codClienteFlow = context.dataStore.data.map { preferences ->
            preferences[COD_CLIENTE_KEY] ?: ""
        }
        hasRequestedPermissionFlow = context.dataStore.data.map { preferences ->
            preferences[HAS_REQUESTED_PERMISSION_KEY] ?: false
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
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