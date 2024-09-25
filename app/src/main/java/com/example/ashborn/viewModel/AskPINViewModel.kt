package com.example.ashborn.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.Operation
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AskPinViewModel( application: Application): AndroidViewModel(application) {
    private val nameClass = AskPinViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    var wrongAttempts by mutableLongStateOf(runBlocking { dsm.wrongAttemptsFlow.first() })
        private set
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    private val userRepository = OfflineUserRepository(ashbornDao)
    private val operationRepository = OperationRepository(ashbornDao)
    var remainingTime: Long by mutableLongStateOf(/*0L*/runBlocking { dsm.timerFlow.first() })
        private set
    var timerIsRunning: Boolean by mutableStateOf(false)
        private set

    fun startTimer() {
        if(!timerIsRunning && wrongAttempts > 3){
            isBlocked = true
            Log.d(nameClass, "start timer view model")
            if(remainingTime <= 0L) remainingTime = calcWaitTime()
            viewModelScope.launch(Dispatchers.Default) {
                timerIsRunning = true
                while (remainingTime > 0) {
                    remainingTime -= 1
                    delay(1000)
                }
                stopTimer()
            }
        }
    }
    var isBlocked: Boolean by mutableStateOf(remainingTime > 0L)
        private set
    val codCliente = run {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
        ris
    }

    private suspend fun stopTimer() {
        timerIsRunning = false
        isBlocked = false
        dsm.writeTimer(0L)
    }

    private fun calcWaitTime(): Long {
        val timesPerWrongAttempts = arrayListOf(60L, 120L, 240L, 480L, 960L, 1920L)
        return if (wrongAttempts <= 3) 0
            else if (wrongAttempts <= 5) timesPerWrongAttempts[0] * (wrongAttempts - 3)
            else if (wrongAttempts <= 10) timesPerWrongAttempts[1] * (wrongAttempts - 5)
            else if (wrongAttempts <= 30) timesPerWrongAttempts[2] * (wrongAttempts - 10)
            else if (wrongAttempts <= 50) timesPerWrongAttempts[3] * (wrongAttempts - 30)
            else if (wrongAttempts <= 100) timesPerWrongAttempts[4] * (wrongAttempts - 50)
            else timesPerWrongAttempts[timesPerWrongAttempts.size - 1] * (wrongAttempts - 100)
    }


    var pin by mutableStateOf("")
        private set

    fun setPinX(pin: String) {
        this.pin = pin
    }

    private fun checkPin(): Boolean {
        return this.pin.length == 8 && this.pin.isDigitsOnly()
    }

    fun validatePin() {

        Log.i("ViewModel", "Pin inserito $pin")
        if (!checkPin()) {
            wrongAttempts++
            writeWrongAttempts()
            Log.i("ViewModel", "formato pin sbagliato: $wrongAttempts")
            if (wrongAttempts % 2L == 0L)
                _navigationEvent.value = NavigationEvent.NavigateToError
            else
                _navigationEvent.value = NavigationEvent.NavigateToErrorAlt
        } else {
            viewModelScope.launch {
                if (!userRepository.isPinCorrect(codCliente, pin.hashCode().toString()).first()) {
                    Log.i("ViewModel", " pin sbagliato")
                    wrongAttempts++
                    if (wrongAttempts % 2L == 0L)
                        _navigationEvent.value = NavigationEvent.NavigateToError
                    else
                        _navigationEvent.value = NavigationEvent.NavigateToErrorAlt
                } else {
                    resetWrongAttempts()
                    viewModelScope.launch(Dispatchers.IO) { dsm.writeTimer(0L) }
                    _navigationEvent.value = NavigationEvent.NavigateToNext
                }
            }
        }
    }

    fun resetWrongAttempts() {
        this.wrongAttempts = 0
        writeWrongAttempts()
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

    fun revocaOperazione(operation: Operation) {
        viewModelScope.launch {
            operationRepository.deleteOperation(operation)
        }
    }

    fun writeWrongAttempts() {
        viewModelScope.launch { dsm.writeWrongAttempts(wrongAttempts) }
    }

    fun formatRemainingTime(): String = "${(remainingTime).toDuration(DurationUnit.SECONDS)}"
    fun writeRemainingTime() {
        viewModelScope.launch(Dispatchers.IO) { dsm.writeTimer(remainingTime) }
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
    data object NavigateToNext: NavigationEvent()
    data object NavigateToError: NavigationEvent()
    data object NavigateToErrorAlt: NavigationEvent()
    data object NavigateToPin: NavigationEvent()
}
