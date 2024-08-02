package com.example.ashborn.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ashborn.data.User
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WelcomeViewModel(dataStoreManager: DataStoreManager) : ViewModel() {
    var dt=dataStoreManager
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
