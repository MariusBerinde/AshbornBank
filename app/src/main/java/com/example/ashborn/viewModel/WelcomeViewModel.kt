package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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