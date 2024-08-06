package com.example.ashborn

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.User
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.view.login.StatoErrore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegistrazioneViewModel( application: Application): AndroidViewModel(application) {
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val userRepository = OfflineUserRepository(ashbornDao)
    var erroreNome by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreCognome by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreDataNascita by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreCodCliente by mutableStateOf(StatoErrore.NESSUNO)
        private set

    var fistLogin: Boolean = true
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification
    var codCliente by mutableStateOf("")
        private set
    var userName by mutableStateOf("")
        private set // Optional: restrict external modification
    var cognome by mutableStateOf("")
        private set // Optional: restrict external modification
    var dataNascita by mutableStateOf( LocalDate
            .now()
            .minusYears(18)
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    )
        private set


    fun setUserNameX(userName: String) {
        this.userName = userName
    }

    fun setCognomeX(cognome:String){
        this.cognome=cognome
    }

    fun setDataNascitaX(dataNascita:String){
        this.dataNascita= dataNascita
    }

    fun setCodClienteX(codCliente:String ){
        this.codCliente=codCliente
    }
    
    fun auth() {
        viewModelScope.launch{
            codCliente=codCliente.replace(" ","")
            val userFromDb = getUserByClientCode(codCliente)
            val validName = userFromDb?.name == userName
            val validSurname = userFromDb?.surname == cognome
            val validDate = userFromDb?.dateOfBirth == dataNascita
            Log.d("ViewModel", "auth")
            erroreCodCliente = if (userFromDb == null) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreNome = if (!validName) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreCognome = if (!validSurname) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreDataNascita = if(!validDate) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            if( validName && validSurname && validDate) {
                Log.d("ViewModel", "auth2")
                _navigationEvent.value = NavigationEvent.NavigateToPin
                Log.d("ViewModel", "auth3")
                //   dataNascita=dataNascita
            } else {
                _navigationEvent.value = NavigationEvent.NavigateToError
                Log.d("ViewModel", "auth4")
            }
        }
    }

    suspend fun getUserByClientCode(clientCode:String): User? {
        var  result:User? = null
        result= CoroutineScope(Dispatchers.IO).async{
            return@async userRepository.getUserByClientCode(clientCode).first()
        }.await()
        Log.d("ViewModel", "getUserByClientCode ${result}")
        return result
    }

    fun writePreferences() {
        viewModelScope.launch (Dispatchers.IO){
            dsm.writeUserPreferences(
                User(
                    name = userName,
                    surname = cognome,
                    clientCode = codCliente,
                    dateOfBirth =  "",
                    pin = ""
                )
            )
        }
    }

    fun setErroreNomeX(erroreNome: StatoErrore) {
        this.erroreNome = erroreNome
    }

    fun setErroreCognomeX(erroreCognome: StatoErrore) {
        this.erroreCognome = erroreCognome
    }

    fun setErroreDataNascitaX(erroreDataNascita: StatoErrore) {
        this.erroreDataNascita = erroreDataNascita
    }

    fun setErroreCodClienteX(erroreCodCliente: StatoErrore) {
        this.erroreCodCliente = erroreCodCliente
    }


}

@Suppress("UNCHECKED_CAST")
class RegistrazioneViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrazioneViewModel::class.java)) {
            return RegistrazioneViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
