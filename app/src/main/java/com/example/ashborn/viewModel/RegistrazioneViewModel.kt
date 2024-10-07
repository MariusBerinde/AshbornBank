package com.example.ashborn.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
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

/**
 * ViewModel for handling user registration and authentication.
 *
 * This ViewModel is responsible for managing the user registration process,
 * including input validation (name, surname, date of birth, and client code),
 * interacting with the local database, and navigating between screens based
 * on the registration result.
 *
 * @param application The application context, used to initialize the database,
 * repositories, and DataStore.
 */
class RegistrazioneViewModel( application: Application): AndroidViewModel(application) {
    /**
     * classname, Logging tag for this class
     */
    val nameClass = RegistrazioneViewModel::class.simpleName
    // LiveData to notify UI about navigation events based on registration status
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    // DataStoreManager instance for storing user preferences
    val dsm = DataStoreManager.getInstance(application)
    // Database DAO and repository for interacting with user data
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val userRepository = OfflineUserRepository(ashbornDao)
    // State variables to hold error states for various input fields
    var erroreNome by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreCognome by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreDataNascita by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreCodCliente by mutableStateOf(StatoErrore.NESSUNO)
        private set
    // Tracks whether this is the first login attempt
    var fistLogin: Boolean = true
    // State variables to hold user input (client code, name, surname, and date of birth)
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

    /**
     * Sets the user's name.
     *
     * @param userName The user's name entered during registration.
     */
    fun setUserNameX(userName: String) {
        this.userName = userName
    }

    /**
     * Sets the user's surname.
     *
     * @param cognome The user's surname entered during registration.
     */
    fun setCognomeX(cognome:String){
        this.cognome=cognome
    }

    /**
     * Sets the user's date of birth.
     *
     * @param dataNascita The user's date of birth entered during registration.
     */
    fun setDataNascitaX(dataNascita:String){
        this.dataNascita= dataNascita
    }

    /**
     * Sets the user's client code.
     *
     * @param codCliente The user's client code entered during registration.
     */
    fun setCodClienteX(codCliente:String ){
        this.codCliente=codCliente
    }

    /**
     * Authenticates the user by validating the entered data against the stored records.
     *
     * This method checks if the entered client code, name, surname, and date of birth
     * match the stored information for the user. It updates error states accordingly and
     * triggers navigation to the PIN screen if validation is successful, or shows an error screen otherwise.
     */
    fun auth() {
        viewModelScope.launch{
            codCliente = codCliente.replace(" ","")
            val userFromDb = getUserByClientCode(codCliente)
            userName = userName.trimEnd().trimStart().replace("  ", " ")
            cognome = cognome.trimEnd().trimStart().replace("  ", " ")
            codCliente = codCliente.replace(" ", "")
            val validName = (userFromDb?.name?.lowercase() ?: "") == userName.lowercase()
            val validSurname = (userFromDb?.surname?.lowercase() ?: "") == cognome.lowercase()
            val validDate = (userFromDb?.dateOfBirth ?: "") == dataNascita
            erroreCodCliente = if (userFromDb == null) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreNome = if (!validName) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreCognome = if (!validSurname) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreDataNascita = if(!validDate) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            if( validName && validSurname && validDate) {
                _navigationEvent.value = NavigationEvent.NavigateToPin
            } else {
                _navigationEvent.value = NavigationEvent.NavigateToError
            }
        }
    }

    /**
     * Retrieves a user from the database by the client code.
     *
     * @param clientCode The client code to search for in the database.
     * @return The [User] object if found, or null otherwise.
     */
    suspend fun getUserByClientCode(clientCode:String): User? {
        val nameFun = object {}.javaClass.enclosingMethod?.name
        var  result: User? = null

        Log.d(nameClass + "," + nameFun, "codice cliente attuale = $clientCode")
        result = CoroutineScope(Dispatchers.IO).async{
            return@async userRepository.getUserByClientCode(clientCode).first()
        }.await()
        Log.d(nameClass + "," + nameFun, "getUserByClientCode ${result}")
        return result
    }

    /**
     * Writes the user's preferences to DataStore.
     *
     * This method saves the user's name, surname, and client code to DataStore
     * for persistence across sessions.
     */
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

    /**
     * Sets the error state for the user's name input.
     *
     * @param erroreNome The error state for the name field.
     */
    fun setErroreNomeX(erroreNome: StatoErrore) {
        this.erroreNome = erroreNome
    }

    /**
     * Sets the error state for the user's surname input.
     *
     * @param erroreCognome The error state for the surname field.
     */
    fun setErroreCognomeX(erroreCognome: StatoErrore) {
        this.erroreCognome = erroreCognome
    }

    /**
     * Sets the error state for the user's date of birth input.
     *
     * @param erroreDataNascita The error state for the date of birth field.
     */
    fun setErroreDataNascitaX(erroreDataNascita: StatoErrore) {
        this.erroreDataNascita = erroreDataNascita
    }

    /**
     * Sets the error state for the user's client code input.
     *
     * @param erroreCodCliente The error state for the client code field.
     */
    fun setErroreCodClienteX(erroreCodCliente: StatoErrore) {
        this.erroreCodCliente = erroreCodCliente
    }


}

/**
 * Factory class for creating instances of [RegistrazioneViewModel].
 *
 * This factory is necessary for providing the [Application] context to the ViewModel.
 *
 * @param application The application context.
 */
@Suppress("UNCHECKED_CAST")
class RegistrazioneViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Create
     *
     * @param modelClass the class of the view model to create
     * @return an instance of the view model
     * @throws IllegalArgumentException
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrazioneViewModel::class.java)) {
            return RegistrazioneViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
