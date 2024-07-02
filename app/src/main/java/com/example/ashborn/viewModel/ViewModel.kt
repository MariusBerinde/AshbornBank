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
                Log.i("ViewModel","Utente nullo")
                Log.i("ViewModel","Utente = ${dataStoreManager.usernameFlow.first().toString()}")
            }else {
                fistLogin=false
                userName = dataStoreManager.usernameFlow.first()
                cognome = dataStoreManager.cognomeFlow.first()
                codCliente = dataStoreManager.codClienteFlow.first()
                Log.i("ViewModel","Utente = ${dataStoreManager.usernameFlow.first().toString()}")
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
                    "987654321".hashCode().toString(),
                    "77777777"
                )
            )
            insertOperation(Operation(1, "777777777", LocalDateTime.now(), LocalDateTime.now(), "pagamento crimini di guerra", 135.89, TransactionType.WITHDRAWAL))
        }

        viewModelScope.launch(Dispatchers.IO) {
            upsertUser(
                User(
                    "Sauron",
                    "Lo oscuro",
                    "1/01/1990",
                    "123456789".hashCode().toString(),
                    "666666666"
                )
            )

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
        fun getUserByClientCode(clientCode:String): User? {


        var  result:User? = null
        viewModelScope.launch {
          /*  userRepository.getUserByClientCode(clientCode).collect { user ->
                result.postValue(user)
            }*/
          // result = userDao.getUserByClientCode(clientCode).first()
            Log.i("VieModel"," con first in getUser = ${result}")
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
            name = userName,
           surname = cognome,
           clientCode = codCliente,
           dateOfBirth =  "",
            pin = ""
        ))
        }
    }

  fun validUser(user: User):Boolean{
        Log.i("ViewModel","sono dentro valid user parametri User={${user.name},${user.surname},${user.clientCode},${user.dateOfBirth}}")
      // val userFromDb:LiveData<User?> = getUserByClientCode(user.clientCode)
      //Log.i("ViewModel","dati da db $userFromDb")


          //getUserByClientCode(user.clientCode)

      /* val validName = userFromDb.value?.name==user.name
       val validSurname = userFromDb.value?.surname == user.surname
       val validDate = userFromDb.value?.dateOfBirth == user.dateOfBirth
        Log.i("ViewModel","validName = $validName \n validSurname = $validSurname\nvalid date = $validDate ")*/
        //        return validName && validSurname && validDate

        return true
    }

    fun validatePin(pin:String):Flow<Boolean>{
        return userRepository.isPinCorrect(codCliente,pin.hashCode().toString())
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
