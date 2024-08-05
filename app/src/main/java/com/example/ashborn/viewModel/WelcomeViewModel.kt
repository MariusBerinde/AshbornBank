package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class  WelcomeViewModel( application: Application): AndroidViewModel(application){
    var dt= DataStoreManager.getInstance(application)
    // val userNameFlow: Flow<String> = dataStoreManager.usernameFlow


    // private val _user = MutableStateFlow<User?>(null)
    // val user: StateFlow<User?> = _user

    //  private val userNameR:Deferred<String> = viewModelScope.async { return@async dataStoreManager.usernameFlow.first() }
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