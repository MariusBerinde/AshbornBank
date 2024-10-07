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
import com.example.ashborn.view.login.StatoErrore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * ViewModel for managing PIN input, validation, and related operations.
 *
 * This ViewModel is used in the composable AskPIN and handles PIN input,
 * validates the PIN, manages the number of wrong attempts, and enforces
 * a blocking mechanism after too many failed attempts. It interacts with
 * [OfflineUserRepository] and [OperationRepository] for authentication and
 * transaction operations.
 *
 * @param application The application context. This is needed to access the [DataStoreManager]
 * and the database through [AshbornDb].
 */
class AskPinViewModel( application: Application): AndroidViewModel(application) {
    private val nameClass = AskPinViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    /**
     * Number of wrong PIN attempts.
     */
    var wrongAttempts by mutableLongStateOf(runBlocking { dsm.wrongAttemptsFlow.first() })
        private set
    /**
     * LiveData for navigation events triggered by PIN validation and other actions.
     */
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    private val userRepository = OfflineUserRepository(ashbornDao)
    private val operationRepository = OperationRepository(ashbornDao)
    /**
     * Remaining time in seconds for the blocking period of the timer.
     */
    private var remainingTime: Long by mutableLongStateOf(/*0L*/runBlocking { dsm.timerFlow.first() })
    /**
     * Indicates if the blocking timer is currently running.
     */
    var timerIsRunning: Boolean by mutableStateOf(false)
        private set
    /**
     * State of the PIN input error.
     */
    var errorePin: StatoErrore by mutableStateOf(StatoErrore.NESSUNO)
        private set

    /**
     * Starts the blocking timer if the user has exceeded the allowed wrong attempts,
     * and it is not already running
     */
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
    /**
     * Indicates if the user is currently blocked due to exceeding wrong attempts.
     */
    var isBlocked: Boolean by mutableStateOf(remainingTime > 0L)
        private set
    /**
     * The client code retrieved from DataStore.
     */
    val codCliente = run {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
        ris
    }

    /**
     * Stops the blocking timer and resets the blocking state.
     */
    private suspend fun stopTimer() {
        timerIsRunning = false
        isBlocked = false
        dsm.writeTimer(0L)
    }

    /**
     * Calculates the waiting time of the blocking timer based on the number of wrong attempts.
     *
     * @return The waiting time in seconds of type Long.
     */
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

    /**
     * The PIN entered by the user.
     */
    var pin by mutableStateOf("")
        private set

    /**
     * Sets the PIN entered by the user.
     *
     * @param pin The inputted PIN.
     */
    fun setPinX(pin: String) {
        this.pin = pin
    }

    /**
     * Checks if the entered PIN is in the correct format (8 digits).
     * Updates [errorePin] accordingly.
     *
     * @return True if the PIN format is correct, false otherwise.
     */
    private fun checkPin(): Boolean {
        val correct = this.pin.length == 8 && this.pin.isDigitsOnly()
        errorePin = if(!correct) StatoErrore.FORMATO else StatoErrore.NESSUNO
        return correct
    }

    /**
     * Validates the entered PIN against the stored hash in the database.
     *
     * If the PIN is incorrect, the wrong attempt count is incremented,
     * and the appropriate error is triggered. If the PIN is correct,
     * the wrong attempts are reset and navigation to the next screen is triggered.
     */
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
                    errorePin = StatoErrore.CONTENUTO
                    if (wrongAttempts % 2L == 0L)
                        _navigationEvent.value = NavigationEvent.NavigateToError
                    else
                        _navigationEvent.value = NavigationEvent.NavigateToErrorAlt
                } else {
                    resetWrongAttempts()
                    viewModelScope.launch(Dispatchers.IO) { dsm.writeTimer(0L) }
                    errorePin = StatoErrore.NESSUNO
                    _navigationEvent.value = NavigationEvent.NavigateToNext
                }
            }
        }
    }

     /**
     * Resets the wrong attempt counter and writes the value to DataStore.
     */
    private fun resetWrongAttempts() {
        this.wrongAttempts = 0
        writeWrongAttempts()
    }
    /**
     * Executes a transaction operation. For the user, the receiver will be completed on the backend
     *
     * @param operation The operation to execute.
     */
    fun executeTransaction(operation: Operation){
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.executeTransaction(operation)
        }
    }

    /**
     * Executes an instant transaction operation asynchronously.
     *
     * @param operation The [Operation] to execute instantly.
     */
    fun executeInstantTransaction(operation: Operation){
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.executeInstantTransaction(operation)
        }
    }
    /**
     * Revokes a pending operation and restores account balance.
     *
     * @param operation The [Operation] to revoke.
     */
    fun revocaOperazione(operation: Operation) {
        viewModelScope.launch {
            operationRepository.deleteOperation(operation)
        }
    }

    /**
     * Writes the current number of wrong attempts to DataStore.
     * This is done for persistence and security reasons.
     */
    fun writeWrongAttempts() {
        viewModelScope.launch { dsm.writeWrongAttempts(wrongAttempts) }
    }
    /**
     * Formats the remaining blocking time into a human-readable string.
     *
     * @return The remaining blocking time as a string in Duration format.
     */
    fun formatRemainingTime(): String = "${(remainingTime).toDuration(DurationUnit.SECONDS)}"
    /**
     * Writes the remaining blocking time to DataStore.
     */
    fun writeRemainingTime() {
        viewModelScope.launch(Dispatchers.IO) { dsm.writeTimer(remainingTime) }
    }
}
/**
 * Factory class for creating instances of [AskPinViewModel].
 *
 * @param application The application context to be passed to the ViewModel.
 */
@Suppress("UNCHECKED_CAST")
class AskPinViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Create
     *
     * @param modelClass the class of the view model to create
     * @return an instance of the view model
     * @throws IllegalArgumentException
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AskPinViewModel::class.java)) {
            return AskPinViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Represents navigation events that are observed by the composable to
 * handle navigation or error-related actions.
 */
sealed class NavigationEvent {
    data object NavigateToNext: NavigationEvent()
    data object NavigateToError: NavigationEvent()
    data object NavigateToErrorAlt: NavigationEvent()
    data object NavigateToPin: NavigationEvent()
}
