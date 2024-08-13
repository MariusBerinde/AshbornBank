package com.example.ashborn.viewModel
import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ashborn.NavigationEvent
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.ErroreUiRegistrazioneStato
import com.example.ashborn.data.Operation
import com.example.ashborn.data.Stato
import com.example.ashborn.data.TransactionType
import com.example.ashborn.data.User
import com.example.ashborn.data.UserState
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.CardRepository
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.OperationRepository
import com.example.ashborn.view.login.StatoErrore
import com.example.ashborn.viewModel.TimeFormatExt.timeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale
import java.util.concurrent.TimeUnit


open class AshbornViewModel(
    application: Application
): AndroidViewModel(application) {
    private val userRepository: OfflineUserRepository
    private val cartaRepositori: CardRepository
    private val contoRepository: ContoRepository
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationState: LiveData<NavigationEvent> = _navigationEvent
    val dataStoreManager: DataStoreManager
    val ashbornDao: AshbornDao

    init {
        ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
        dataStoreManager = DataStoreManager.getInstance(application)
        cartaRepositori = CardRepository(ashbornDao)
        userRepository = OfflineUserRepository(ashbornDao)
        contoRepository = ContoRepository(ashbornDao)
        initDb()
        viewModelScope.launch{
            if(dataStoreManager.usernameFlow.first().isEmpty() ){
                fistLogin=true
            } else {
                fistLogin=false
                userName = dataStoreManager.usernameFlow.first()
                cognome = dataStoreManager.cognomeFlow.first()
                codCliente = dataStoreManager.codClienteFlow.first()
                getConti()
                getCarte()

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
        insertConto(Conto(codConto = "42",codCliente ="777777777", stato = Stato.ATTIVO, iban = "IT1234567890123456789012345",saldo = 190000.00 ))
            insertConto(Conto(codConto = "43",codCliente ="666666666", stato = Stato.ATTIVO , iban = "IT1234567890123456789012345",saldo = 200000.000))
            insertCarta(Carta(
                nrCarta = 1111222233334444,
                dataScadenza = LocalDateTime.of(2030,7,25,0,0), //"25/07/2300",
                codUtente = "777777777",
                statoCarta = Stato.ATTIVO,
                codConto = "42",
                cvc = "732",
                saldo = 190000.00
            ))
            }
/*
        viewModelScope.launch(Dispatchers.IO) {
            insertOperation(Operation( clientCode ="777777777", dateO = LocalDateTime.now(), dateV = LocalDateTime.now(), description = "Pagamento crimini di guerra", amount =  135.89, operationType = TransactionType.WITHDRAWAL, bankAccount = "42", cardCode = null, recipient="", iban = ""))
            insertOperation(Operation( clientCode ="777777777", dateO = LocalDateTime.now().minusDays(1),dateV = LocalDateTime.now().minusDays(1),description = "Pagamento Bolletta Luce", amount =  92.00, operationType = TransactionType.WITHDRAWAL, bankAccount = "42", cardCode = "1111222233334444", recipient="", iban = "" ))
            insertOperation(Operation( clientCode ="777777777", dateO = LocalDateTime.now().minusDays(1),dateV = LocalDateTime.now().minusDays(1),description ="Pagamento per Mutuo del male", amount =  92.00, operationType = TransactionType.WITHDRAWAL,bankAccount = "42", cardCode = "1111222233334444", recipient="", iban = "" ))
        }*/
    }

    var statoUtente by mutableStateOf(UserState())
        private set

    var statoUtenteUi by mutableStateOf(UserState())
        private set

    var erroreUiRegistrazioneStato by mutableStateOf(ErroreUiRegistrazioneStato())
        private set

    fun upsertUser(utente: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.upsertUser(utente)
        }
    }
    fun insertCarta(carta: Carta){
        viewModelScope.launch(Dispatchers.IO) {
           cartaRepositori.insertCarta(carta)
        }
    }

    fun insertConto(conto: Conto){
        viewModelScope.launch(Dispatchers.IO) {
           contoRepository.inserisciConto(conto)
        }
    }
    fun deleteUser(utente: User) {
        viewModelScope.launch(Dispatchers.IO) {
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
    var listaCarte by mutableStateOf(
        arrayListOf<Carta>(Carta(
            nrCarta = 1111222233334444,
            dataScadenza = LocalDateTime.of(2030,7,25,0,0),// "20/04/2300",
            codUtente = "777777777",
            statoCarta = Stato.ATTIVO,
            codConto = "42",
            saldo = 0.0,
            cvc = "777"
        ))
    )
    var listaConti by mutableStateOf(
        arrayListOf<Conto>(
            Conto("777777777777","777 777 777",0.0, "IT1234567890123456789012345", Stato.ATTIVO)
        )
    )
    var arrayOperazioniCarte: ArrayList<Operation>
        get() = arrayListOf(
            Operation(
                id=0,
                clientCode = "1",
                dateO = LocalDateTime.now(),
                dateV = LocalDateTime.now(),
                description = "Pagamento Bolletta",
                //CurrencyAmount(167.00, Currency.getInstance("EUR")),
                amount = 167.00,
                operationType = TransactionType.WITHDRAWAL,
                bankAccount = "42",
                cardCode = "1111222233334444",
                iban = "",
                recipient = ""
            )

        )
        set(value) = TODO()
    var arrayOperazioniConto: ArrayList<Operation>
        get() = arrayListOf(
            Operation(
                id = 0,
                clientCode = "1",
                dateO = LocalDateTime.now(),
                dateV = LocalDateTime.now(),
                description = "Pagamento Bolletta",
                //CurrencyAmount(167.00, Currency.getInstance("EUR")),
                amount = 167.00,
                operationType = TransactionType.WITHDRAWAL,
                bankAccount = "42",
                cardCode = null,
                iban = "",
                recipient = "",
            )

            )
        set(value) = TODO()
    var operazioniCarta by mutableStateOf(arrayOperazioniCarte) //TODO da ren
    var operazioniConto by mutableStateOf(arrayOperazioniConto)
    var contoMostrato by mutableStateOf(
        Conto("xxxx xxx xxx","XXX XXX XXX",0.0, "fdagfkldnaògad",Stato.ATTIVO)
)
    var cartaMostrata by mutableStateOf(Carta(
                        nrCarta = 1234123412341234,
                        dataScadenza = LocalDateTime.of(2030,7,25,0,0),//"20/04/2300",
                        codUtente = "0000000000000",
                        statoCarta = Stato.ATTIVO,
                        codConto = "420",
                        saldo = 0.0,
                        cvc = "777asd")
    )

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
            dataStoreManager.writeUserPreferences(User(
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
            _navigationEvent.value = NavigationEvent.NavigateToError
            wrongAttempts++
        } else {
            viewModelScope.launch {
                if(!userRepository.isPinCorrect(codCliente, pin.hashCode().toString()).first()) {

                    Log.i("ViewModel"," pin sbagliato")

                    _navigationEvent.value = NavigationEvent.NavigateToError
                    wrongAttempts++
                } else {
                    _navigationEvent.value = NavigationEvent.NavigateToNext
                }
            }
        }
    }

    fun auth(){
        viewModelScope.launch{
            codCliente=codCliente.replace(" ","")
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
                _navigationEvent.value = NavigationEvent.NavigateToPin
                Log.d("ViewModel", "auth3")
             //   dataNascita=dataNascita
            } else {
                _navigationEvent.value = NavigationEvent.NavigateToError
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
            dataStoreManager.writeUserPreferences(User("","","","",""))
        }
    }

    //todo: da testare e inserire in init
    fun getConti(){

        viewModelScope.launch(Dispatchers.IO) {
            var datiDb=viewModelScope.async(Dispatchers.IO) {
                return@async    contoRepository.getConti(codCliente)
            }.await()

            listaConti = datiDb.first().toCollection(ArrayList<Conto>())
            contoMostrato = listaConti.get(0)
            getOperationsConto()
        }

    }
    //todo: da testare e inserire in init
    fun getCarte(){
        viewModelScope.launch(Dispatchers.IO) {
            var datiDb=viewModelScope.async(Dispatchers.IO) {
                return@async    cartaRepositori.getCarte(codCliente)
            }.await()

            listaCarte = datiDb.first().toCollection(ArrayList<Carta>())
            cartaMostrata = listaCarte.get(0)
            getOperationsCarta()
        }
    }

    //todo: da testare e inserire in init
    fun getOperationsConto() {
        Log.d("viewModel","getOpConto nrCarta=${contoMostrato.codConto}, from= ${LocalDateTime.now().minusDays(30)}, upTo=${LocalDateTime.now()}")
        viewModelScope.launch(Dispatchers.IO) {
            val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                return@async operationRepository.getOperazioniConto(contoMostrato.codConto, LocalDateTime.now().minusDays(30), LocalDateTime.now(),0, 10)
            }.await()
            operazioniConto = operazioniDb.first().toCollection(ArrayList<Operation>())
            Log.d("ViewModel", "getOperationConto : $operazioniConto")
        }
    }
//todo: da testare e inserire in init
    fun getOperationsCarta() {
       Log.d("viewModel","getOpConto nrCarta=${cartaMostrata.nrCarta}, from= ${LocalDateTime.now().minusDays(30)}, upTo=${LocalDateTime.now()}")
        viewModelScope.launch(Dispatchers.IO) {
            val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                return@async operationRepository.getOperazioniCarta(cartaMostrata.nrCarta, LocalDateTime.now().minusDays(30), LocalDateTime.now(),0, 10)
            }.await()
            //operazioniCarta = operazioniDb.first().toCollection(ArrayList<Operation>())
            operazioniCarta = operazioniDb.first().toCollection(ArrayList<Operation>())
            Log.d("ViewModel", "getOperationCarta: $operazioniCarta")
        }
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
