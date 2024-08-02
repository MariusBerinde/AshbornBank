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

class DataStoreManager(val context: Context){

    // per datastore
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    // implementazione lettura preferenze
    private val USERNAME_KEY = stringPreferencesKey("username_key")
    private val COGNOME_KEY = stringPreferencesKey("cognome_key")
    private val COD_CLIENTE_KEY= stringPreferencesKey("cod_cliente_key")


    val usernameFlow: Flow<String> = context.dataStore
        .data.map{
                preferences -> preferences[USERNAME_KEY]?:""
        }
    val cognomeFlow: Flow<String> = context.dataStore
        .data.map{
                preferences -> preferences[COGNOME_KEY]?:""
        }

    val codClienteFlow: Flow<String> = context.dataStore
        .data.map{
                preferences -> preferences[COD_CLIENTE_KEY]?:""
        }

    suspend fun writeUsername(userName: String){
        context.dataStore.edit {
                settings -> settings[USERNAME_KEY]=userName
        }
    }

    suspend fun writeCognome(cognome:  String){
        context.dataStore.edit {
                settings -> settings[COGNOME_KEY]=cognome
        }
    }
    suspend fun writeCodCliente(codCliente: String){
        context.dataStore.edit {
                settings -> settings[COD_CLIENTE_KEY]=codCliente
        }
    }

    suspend fun writeUserPrefernces(user: User){
        writeCognome(user.surname)
        writeUsername(user.name)
        writeCodCliente(user.clientCode)
    }
}