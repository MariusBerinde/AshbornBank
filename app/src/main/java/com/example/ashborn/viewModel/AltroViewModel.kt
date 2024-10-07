package com.example.ashborn.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.User
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * View model of composable Altro, makes use of
 *
 * @constructor creates an AltroViewModel instance
 *
 * @param application an application context (needed for datastoremanager)
 */
@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class AltroViewModel(application: Application): AndroidViewModel(application) {
    /**
     * The name of the class, used mainly for logging purposes
     */
    private val nameClass = AltroViewModel::class.simpleName

    /**
     * DataStoreManager instance used for reading and writing preferences in DataStore
     */
    val dsm = DataStoreManager.getInstance(application)
    /**
     * The user's first name retrieved from DataStore.
     */
    var userName = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.usernameFlow.first().also { ris = it }
        }
    }
     /**
     * The user's last name retrieved from DataStore.
     */
    var cognome = runBlocking {
        var ris:String
        runBlocking(Dispatchers.IO) {
            dsm.cognomeFlow.first().also { ris = it }
        }
    }
     /**
     * Deletes all user preferences and resets related data in DataStore.
     *
     * This function clears the following data:
     * - Name
     * - Surname
     * - Client code
     * - Date of birth
     * - PIN
     * - Timer
     * - Wrong attempts count
     * - Permission request status
     */
    fun deletePreferences() {
        viewModelScope.launch (Dispatchers.IO){
            dsm.writeUserPreferences(
                User(
                    name = "",
                    surname = "",
                    clientCode = "",
                    dateOfBirth =  "",
                    pin = ""
                )
            )
            userName = ""
            cognome = ""
            dsm.writeTimer(0L)
            dsm.writeWrongAttempts(0L)
            dsm.writeHasRequestedPermission(false)
            dsm.reload()
            Log.i(nameClass, "Smartphone reset")
        }
    }
}

/**
 * Altro view model factory
 *
 * @property application
 * @constructor Create empty Altro view model factory
 * @throws IllegalArgumentException
 */
@Suppress("UNCHECKED_CAST")
class AltroViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AltroViewModel::class.java)) {
            return AltroViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}