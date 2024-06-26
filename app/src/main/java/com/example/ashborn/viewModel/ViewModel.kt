package com.example.ashborn.viewModel
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.ErroreUiRegistrazioneStato
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import com.example.ashborn.data.User
import com.example.ashborn.data.UserState
import com.example.ashborn.db.UserDb
import com.example.ashborn.repository.OfflineUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

open class AshbornViewModel(
    application: Application
): AndroidViewModel(application) {


     private val userRepository: OfflineUserRepository


     val dataStoreManager:DataStoreManager = DataStoreManager(application)

    init {
        val userDao = UserDb.getDatabase(application).userDao()
        userRepository = OfflineUserRepository(userDao)
        viewModelScope.launch{
           // viewModelScope.launch{
            if(dataStoreManager.usernameFlow.first().isEmpty() ){
                fistLogin=true
                Log.i("ViewModel","Utente nullo")
                Log.i("ViewModel","Utente = ${dataStoreManager.usernameFlow.first().toString()}")
            }else {
                fistLogin=false
                Log.i("ViewModel","Utente = ${dataStoreManager.usernameFlow.first().toString()}")
            }
        }


    }
   var statoUtente by mutableStateOf(UserState())
       private set

    var statoUtenteUi by mutableStateOf(UserState())
        private set
    var erroreUiRegistrazioneStato by mutableStateOf(ErroreUiRegistrazioneStato())
        private set


    fun upsertUser(utente: User) {
        viewModelScope.launch {
            userRepository.upsertUser(utente)
        }
    }

    fun deleteUser(utente: User) {
        viewModelScope.launch {
            userRepository.deleteUser(utente)
        }
    }

    fun getUserById(id: String): LiveData<User?> {
        val result = MutableLiveData<User?>()

        viewModelScope.launch {
            userRepository.getUserById(id).collect { user ->
                result.postValue(user)
            }
        }

        return result
    }



    val tag: String = AshbornViewModel::class.java.simpleName
    var erroreNome by mutableIntStateOf(0)
        private set
    var erroreCognome by mutableIntStateOf(0)
        private set
    var erroreDataNascita by mutableIntStateOf(0)
        private set
    var erroreCodCliente by mutableIntStateOf(0)
        private set
    val iban: String = "IT1234567890123456789012345"
    var saldo by mutableDoubleStateOf(0.0)
        private set
    var codConto: String = "0987654321"
        private set
    var fistLogin: Boolean = true
    var startDest: String = "init"
        private set
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification
    var pin by mutableStateOf((""))
        private set // Optional: restrict external modification

    var codCliente by mutableStateOf("")
        private set
    var userName by mutableStateOf("")
        private set // Optional: restrict external modification
    var cognome by mutableStateOf("")
        private set // Optional: restrict external modification
    var dataNascita by mutableStateOf("")
        private set
    var arrayOperazioni: ArrayList<Operation>
        get() = arrayListOf(
            Operation(
                0,
                "1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Pagamento Bolletta",
                //CurrencyAmount(167.00, Currency.getInstance("EUR")),
                167.00,
                TransactionType.WITHDRAWAL
            ),
            Operation(
                1,
                "1",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(1),
                "Pagamento Bolletta",
                //CurrencyAmount(92.00, Currency.getInstance("EUR")),
                92.00,
                TransactionType.WITHDRAWAL
            ),
            Operation(
                3,
                "1",
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(3),
                "Pagamento Bolletta",
                147.00,
                TransactionType.WITHDRAWAL
            ),
        )
        set(value) = TODO()
    var operazioni by mutableStateOf(arrayOperazioni)
    fun setPinX(pin: String) {
        this.pin = pin
    }
    fun setUserNameX(userName: String) {
        Log.i(tag,"valore username $userName")
        this.userName = userName
    }
    fun setCognomeX(cognome:String){
        this.cognome=cognome
        Log.i(tag,"valore cognome $cognome")
    }
    fun setDataNascitaX(dataNascita:String){
        Log.i(tag,"valore data di Nascita $dataNascita")
        this.dataNascita= dataNascita
    }
    fun setCodClienteX(codCliente:String ){
        Log.i(tag,"valore codice cliente $codCliente")
        this.codCliente=codCliente
    }
    fun setStartdest(startDest: String){
        if (startDest == "init" || startDest == "principale") {
            this.startDest= startDest
        }
    }
    fun checkPin(): Boolean {
        return this.pin.length == 8 && this.pin.isDigitsOnly()
    }
    fun incrementWrongAttempts() {
        this.wrongAttempts++
    }


    fun setErroreNomeX(b: Boolean) {
        this.erroreNome = if (b) {1} else {0}
    }

    fun setErroreCognomeX(b: Boolean) {
        this.erroreCognome = if (b) {1} else {0}
    }

    fun setErroreDataNX(b: Boolean) {
        this.erroreDataNascita = if (b) {1} else {0}
    }
    fun setErroreCodClienteX(b:Boolean){
        this.erroreCodCliente = if (b) {1} else {0}
    }

    fun writePreferences(){
      Log.i("ViewModel","Lancio writePreferences")
        viewModelScope.launch(Dispatchers.IO){
        dataStoreManager.writeUserPrefernces(User(
            userName,
            cognome,
            codCliente,
            dataNascita
        ))
        }
    }

}

class DataStoreManager(val context: Context){

    // per datastore
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    // implementazione lettura preferenze
    private val USERNAME_KEY = stringPreferencesKey("username_key")
    private val COGNOME_KEY = stringPreferencesKey("cognome_key")
    private val COD_CLIENTE_KEY= stringPreferencesKey("cod_cliente_key")


    val usernameFlow: Flow<String> = context.dataStore
        .data.map{
                preferences -> preferences[USERNAME_KEY]?:""
        }
    val cognomeFlow: Flow<String> = context.dataStore
        .data.map{
                preferences -> preferences[COGNOME_KEY]?:""
        }

    val codClienteFlow: Flow<String> = context.dataStore
        .data.map{
                preferences -> preferences[COD_CLIENTE_KEY]?:""
        }

    suspend fun writeUsername(userName: String){
        Log.i("DataStorage","scrivo"+userName)
        context.dataStore.edit {
            settings -> settings[USERNAME_KEY]=userName

        }

    }

     suspend fun writeCognome(cognome:  String){
        context.dataStore.edit {

            settings -> settings[COGNOME_KEY]=cognome
        }
    }
    suspend fun writeCodCliente(codCliente: String){
        context.dataStore.edit {

            settings -> settings[COD_CLIENTE_KEY]=codCliente
        }
    }

    suspend fun writeUserPrefernces(user: User){

            writeCognome(user.surname)
            writeUsername(user.name)
            writeCodCliente(user.clientCode)

    }

}
