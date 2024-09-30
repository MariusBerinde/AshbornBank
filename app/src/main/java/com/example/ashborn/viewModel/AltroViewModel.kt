package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.User
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AltroViewModel(application: Application): AndroidViewModel(application) {
    val nameClass = ContiViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    var userName = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.usernameFlow.first().also { ris = it }
        }
    }
    var cognome = runBlocking {
        var ris:String
        runBlocking(Dispatchers.IO) {
            dsm.cognomeFlow.first().also { ris = it }
        }
    }

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
        }
    }
}

@Suppress("UNCHECKED_CAST")
class AltroViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AltroViewModel::class.java)) {
            return AltroViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}