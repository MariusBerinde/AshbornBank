package com.example.ashborn.viewModel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class  WelcomeViewModel(application: Application): AndroidViewModel(application){
    var dt= DataStoreManager.getInstance(application)
    var userName = ""
    fun getUsername():String{
        var ris = ""
        runBlocking(Dispatchers.IO) {
            ris = dt.usernameFlow.first()
        }
        return  ris.toString()
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