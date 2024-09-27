package com.example.ashborn.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class  WelcomeViewModel(application: Application): AndroidViewModel(application){

    var dsm= DataStoreManager.getInstance(application)
    var hasRequestedPermission by mutableStateOf(
        runBlocking(Dispatchers.IO) {
            dsm.hasRequestedPermissionFlow.first()
        }
    )
        private set
    var userName = ""
  
    fun getUsername():String{
        var ris: String
        runBlocking(Dispatchers.IO) { ris = dsm.usernameFlow.first() }
        return ris
    }

    fun setHasRequestedPermissionX(hasRequestedPermission : Boolean) {
        this.hasRequestedPermission = hasRequestedPermission
        viewModelScope.launch (Dispatchers.IO){
            dsm.writeHasRequestedPermission(hasRequestedPermission)
        }
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