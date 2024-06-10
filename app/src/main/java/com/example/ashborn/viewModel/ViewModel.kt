package com.example.ashborn.viewModel

import android.icu.util.Currency
import android.icu.util.CurrencyAmount
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import java.time.LocalDateTime

class AshbornViewModel: ViewModel() {
    val iban: String = "IT1234567890123456789012345"
    var saldo: Double = 0.0
        private set
    var codConto: String = "0987654321"
        private set

    val tag: String = AshbornViewModel::class.java.simpleName
    var fistLogin: Boolean = false
    var startDest: String = "init"
        private set
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification

    var pin by mutableStateOf((""))
        private set // Optional: restrict external modification

    var codCliente by mutableStateOf("")
        private set

    var userName by mutableStateOf("Mariolone Bubbarello")
        private set // Optional: restrict external modification

    var cognome by mutableStateOf("")
        private set // Optional: restrict external modification

    var dataNascita by  mutableStateOf("")
        private set
    var arrayOperazioni:ArrayList<Operation> = arrayListOf(
        Operation(
            0,
            "1",
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Pagamento Bolletta",
            CurrencyAmount(167.00, Currency.getInstance("EUR")),
            TransactionType.WITHDRAWAL
        ),
        Operation(
            1,
            "1",
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Pagamento Bolletta",
            CurrencyAmount(92.00, Currency.getInstance("EUR")),
            TransactionType.WITHDRAWAL
        ),
        Operation(
            3,
            "1",
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Pagamento Bolletta",
            CurrencyAmount(147.00, Currency.getInstance("EUR")),
            TransactionType.WITHDRAWAL
        ),
    )

    fun setPinX(pin: String) {
        this.pin = pin
    }

    fun setUserNameX(userName: String) {
        Log.i(tag,"valore username $userName")
        this.userName = userName
    }

    fun setCognome(cognome:String){
        this.cognome=cognome
        Log.i(tag,"valore cognome $cognome")
    }

    fun setDataNascita(dataNascita:String){
        Log.i(tag,"valore data di Nascita $dataNascita")
        this.dataNascita= dataNascita
    }

    fun setCodcliente(codCliente:String ){
        Log.i(tag,"valore codice cliente $codCliente")
        this.codCliente=codCliente
    }

    fun setStartdest(startDest: String){
        if (startDest == "init" || startDest == "principale") {
            this.startDest= startDest
        }

    }
    fun checkPin(): Boolean {
        return false
    }

    fun incrementWrongAttempts() {
        this.wrongAttempts++
    }

}