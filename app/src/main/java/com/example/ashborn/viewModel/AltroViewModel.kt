package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AltroViewModel(application: Application): AndroidViewModel(application) {
    val networkConnectivityObserver = NetworkConnectivityObserver.getInstance(application)
    val nameClass = ContiViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val userName = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.usernameFlow.first().also { ris = it }
        }
    }
    val cognome = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.cognomeFlow.first().also { ris = it }
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