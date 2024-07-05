package com.example.ashborn.viewModel
import android.app.Application
import android.content.Context
import android.os.CountDownTimer
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
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.ErroreUiRegistrazioneStato
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import com.example.ashborn.data.User
import com.example.ashborn.data.UserState
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.OperationRepository
import com.example.ashborn.view.login.StatoErrore
import com.example.ashborn.viewModel.TimeFormatExt.timeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale
import java.util.concurrent.TimeUnit

open class AshbornViewModel(
    application: Application
): AndroidViewModel(application) {
    private val userRepository: OfflineUserRepository
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    val dataStoreManager:DataStoreManager
    val ashbornDao: AshbornDao

    init {
        ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
        dataStoreManager = DataStoreManager(application)
        userRepository = OfflineUserRepository(ashbornDao)
        initDb()
        viewModelScope.launch{
            if(dataStoreManager.usernameFlow.first().isEmpty() ){
                fistLogin=true
            } else {
                fistLogin=false
                userName = dataStoreManager.usernameFlow.first()
                cognome = dataStoreManager.cognomeFlow.first()
                codCliente = dataStoreManager.codClienteFlow.first()
                getOperations()
            }
        }
    }

    fun initDb(){
        viewModelScope.launch(Dispatchers.IO) {
            upsertUser(
                User(
                    "Tom",
                    "Riddle",
                    "1/01/1990",
                    "87654321".hashCode().toString(),
                    "777777777"
                )
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            upsertUser(
                User(
                    "Sauron",
                    "Lo oscuro",
                    "1/01/1990",
                    "12345678".hashCode().toString(),
                    "666666666"
                )
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            insertOperation(Operation( clientCode ="777777777", dateO = LocalDateTime.now(), dateV = LocalDateTime.now(), description = "Pagamento crimini di guerra", amount =  135.89, operationType = TransactionType.WITHDRAWAL))
            insertOperation(Operation( clientCode ="777777777", dateO = LocalDateTime.now().minusDays(1),dateV = LocalDateTime.now().minusDays(1),description = "Pagamento Bolletta Luce", amount =  92.00, operationType = TransactionType.WITHDRAWAL))
            insertOperation(Operation( clientCode ="777777777", dateO = LocalDateTime.now().minusDays(1),dateV = LocalDateTime.now().minusDays(1),description ="Pagamento per Mutuo del male", amount =  92.00, operationType = TransactionType.WITHDRAWAL))
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
    //TODO: da togliere
    private val operationRepository:OperationRepository = OperationRepository(ashbornDao)
    private fun insertOperation(operation: Operation) {
        viewModelScope.launch {
            operationRepository.insertOperation(operation)
        }
    }


    //fun getUserByClientCode(clientCode:String): LiveData<User?> {
    suspend fun getUserByClientCode(clientCode:String): User? {
        var  result:User? = null
        result= CoroutineScope(Dispatchers.IO).async{
            return@async userRepository.getUserByClientCode(clientCode).first()
        }.await()
        Log.d("ViewModel", "getUserByClientCode ${result}")
        return result
    }

    val tag: String = AshbornViewModel::class.java.simpleName
    var erroreNome by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreCognome by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreDataNascita by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreCodCliente by mutableStateOf(StatoErrore.NESSUNO)
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

    var operazioni by mutableStateOf(arrayOperazioni/*listOf<Operation>()*/)

    val ore = TimeUnit.HOURS.toMillis(0)
    val minuti = TimeUnit.HOURS.toMillis(0)
    val secondi = TimeUnit.HOURS.toMillis(5)
    val tempoRimanenteIniziale = ore + minuti + secondi
    var tempoRimanente = mutableStateOf(tempoRimanenteIniziale)
    var testoTimer = mutableStateOf(tempoRimanente.value.timeFormat())
    val intervalloContoRovescia = 1000L
    var countDownTimer: CountDownTimer? = null

    fun lanciaTimer() = viewModelScope.launch {
        countDownTimer = object :CountDownTimer(tempoRimanente.value, intervalloContoRovescia) {
            override fun onTick(tempoRimanenteAttuale: Long) {
                testoTimer.value = tempoRimanenteAttuale.timeFormat()
                tempoRimanente.value = tempoRimanenteAttuale
            }

            override fun onFinish() {
                testoTimer.value = tempoRimanenteIniziale.timeFormat()

            }
        }.start()
    }

    fun fermaTimer() = viewModelScope.launch { countDownTimer?.cancel() }
    fun setPinX(pin: String) {
        this.pin = pin
    }

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

    fun resetWrongAttempts() {
        this.wrongAttempts = 0
    }
    fun setErroreNomeX(stato:Boolean) {
       this.erroreNome = if(stato)  StatoErrore.NESSUNO else StatoErrore.FORMATO
    }

    fun setErroreCognomeX(stato: Boolean) {
        this.erroreCognome = if(stato)  StatoErrore.NESSUNO else StatoErrore.FORMATO
    }

    fun setErroreDataNX(stato: Boolean) {
        this.erroreDataNascita = if(stato)  StatoErrore.NESSUNO else StatoErrore.FORMATO
    }

    fun setErroreCodClienteX(stato: Boolean){
        this.erroreCodCliente = if(stato)  StatoErrore.NESSUNO else StatoErrore.FORMATO
    }

    fun writePreferences(){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreManager.writeUserPrefernces(User(
                name = userName,
                surname = cognome,
                clientCode = codCliente,
                dateOfBirth =  "",
                pin = ""
            ))
        }
    }

    fun validatePin(){
        if(!checkPin()) {
            Log.i("ViewModel","formato pin sbagliato")
            _navigationEvent.value = NavigationEvent.NavagateToError
            wrongAttempts++
        } else {
            viewModelScope.launch {
                if(!userRepository.isPinCorrect(codCliente, pin.hashCode().toString()).first()) {

                    Log.i("ViewModel"," pin sbagliato")

                    _navigationEvent.value = NavigationEvent.NavagateToError
                    wrongAttempts++
                } else {
                    _navigationEvent.value = NavigationEvent.NavagateToConti
                }
            }
        }
    }

    fun auth(){
        viewModelScope.launch{
            val userFromDb = getUserByClientCode(codCliente)
            val validName = userFromDb?.name==userName
            val validSurname = userFromDb?.surname == cognome
            val validDate = userFromDb?.dateOfBirth == dataNascita
            Log.d("ViewModel", "auth")
            erroreCodCliente = if (userFromDb == null) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreNome = if (!validName) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreCognome = if (!validSurname) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            erroreDataNascita = if(!validDate) StatoErrore.CONTENUTO else StatoErrore.NESSUNO
            if( validName && validSurname && validDate) {
                Log.d("ViewModel", "auth2")
                _navigationEvent.value = NavigationEvent.NavagateToConti
                Log.d("ViewModel", "auth3")
            } else {
                _navigationEvent.value = NavigationEvent.NavagateToError
                Log.d("ViewModel", "auth4")
            }
        }
    }

    fun cancellaPreferenzeLocali(){
        fistLogin = true
        userName = ""
        cognome = ""
        dataNascita = ""
        codCliente = ""

        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.writeUserPrefernces(User("","","","",""))
        }


    }

    fun getOperations() {
        viewModelScope.launch(Dispatchers.IO) {
            val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                return@async operationRepository.getOperations(codCliente, LocalDateTime.now().minusDays(30), LocalDateTime.now(),0, 10)
            }.await()
            operazioni = operazioniDb.first().toCollection(ArrayList<Operation>())
            Log.d("ViewModel", "getOperations operazioni: $operazioni")
        }
    }
}

sealed class NavigationEvent {
    object NavagateToConti:NavigationEvent()
    object NavagateToError:NavigationEvent()

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

object TimeFormatExt {
    private const val FORMAT = "%02d:%02d:%02d"

    fun Long.timeFormat(): String = String.format(
        Locale.getDefault(),
        FORMAT,
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) % 60,
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )
}