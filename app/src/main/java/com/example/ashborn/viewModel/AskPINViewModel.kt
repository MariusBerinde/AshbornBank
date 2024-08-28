package com.example.ashborn

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.Operation
import com.example.ashborn.data.User
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AskPinViewModel( application: Application): AndroidViewModel(application) {
    val nameClass = AskPinViewModel::class.simpleName
    var wrongAttempts by mutableIntStateOf(0)
        private set
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val userRepository = OfflineUserRepository(ashbornDao)
    val operationRepository = OperationRepository(ashbornDao)
    val codCliente = run {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
        Log.d(nameClass,"valore di codCliente = $ris")
        ris.toString()
    }


    var pin by mutableStateOf("")
        private set

    fun setPinX(pin: String) {
        this.pin = pin
    }

    fun checkPin(): Boolean {
        return this.pin.length == 8 && this.pin.isDigitsOnly()
    }

    fun validatePin() {
        if (!checkPin()) {
            Log.i("ViewModel", "formato pin sbagliato")
            _navigationEvent.value = NavigationEvent.NavigateToError
            wrongAttempts++
        } else {
            viewModelScope.launch {
                if (!userRepository.isPinCorrect(codCliente, pin.hashCode().toString()).first()) {
                    Log.i("ViewModel", " pin sbagliato")
                    _navigationEvent.value = NavigationEvent.NavigateToError
                    wrongAttempts++
                } else {
                    _navigationEvent.value = NavigationEvent.NavigateToNext
                }
            }
        }

        suspend fun getUserByClientCode(clientCode: String): User? {
            var result: User? = null
            Log.d(nameClass,"getUserbyClient valore di client code : $clientCode")
            result = CoroutineScope(Dispatchers.IO).async {
                return@async userRepository.getUserByClientCode(clientCode).first()
            }.await()
            Log.d("ViewModel", "getUserByClientCode ${result}")
            return result
        }


    }

    fun resetWrongAttempts() {
        this.wrongAttempts = 0
    }
    fun saveOperation(operation: Operation) {
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.insertOperation(operation)
        }
    }

    fun executeTransaction(operation: Operation){
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.executeTransaction(operation)
        }
    }

    fun executeInstantTransaction(operation: Operation){
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.executeInstantTransaction(operation)
        }
    }
}
@Suppress("UNCHECKED_CAST")
class AskPinViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AskPinViewModel::class.java)) {
            return AskPinViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class NavigationEvent {
    object NavigateToNext: NavigationEvent()
    object NavigateToError: NavigationEvent()
    object NavigateToPin: NavigationEvent()
}
