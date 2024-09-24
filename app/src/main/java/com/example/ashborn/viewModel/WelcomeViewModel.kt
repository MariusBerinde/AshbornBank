package com.example.ashborn.viewModel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class  WelcomeViewModel(application: Application): AndroidViewModel(application){
  
    var dsm= DataStoreManager.getInstance(application)
    var userName = ""
  
    fun getUsername():String{
        var ris: String
        runBlocking(Dispatchers.IO) { ris = dsm.usernameFlow.first() }
        return ris
    }
}

@Suppress("UNCHECKED_CAST")
class WelcomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}