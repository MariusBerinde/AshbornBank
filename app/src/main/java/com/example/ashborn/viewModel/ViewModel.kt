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
import java.util.ArrayList

open class AshbornViewModel: ViewModel() {
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
    var arrayOperazioni: ArrayList<Operation>
        get() = arrayListOf(
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
        set(value) = TODO()

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

    /**
     * Controlla il formato di dataNascita , il formato è considerato valido se rispetta il pattern giorno/mese/anno o giorno-mese-anno
     * @param dataNascita : la data di nascita del cliente
     * @return true se data di nascita ha un formato valido
     */
    fun formatoDataNascitaValida(dataNascita: String): Boolean{
        var ris:Boolean = false
        val pattern= "\\d{2}-\\d{2}-\\d{4}||\\d{2}/\\d{2}/\\d{4}"

        if (Regex(pattern).matches(dataNascita)){
            val day=dataNascita.substring(0,2).toInt()
            val month=dataNascita.substring(3,5).toInt()
            val year=dataNascita.substring(6,dataNascita.length).toInt()
            if(year >= 1900){
                when(month){
                    1,3,5,7,8,10,12 ->ris=if(day>0 && day<=31) true else false
                    4,6,9,11 -> ris=if(day>0 && day<=30) true else false
                    2 -> if( (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){ris=if(day>0 && day<=29) true else false}else{ris=if(day>0 && day<=28) true else false}
                    else -> ris=false
                }

            }
        }

        return ris
    }

    fun formatoCodiceCliente(codCliente: String): Boolean {
        var ris:Boolean = false
        if( codCliente.length == 9){
            val regex = Regex("[^a-z0-9]")
            ris = !regex.containsMatchIn(codCliente)
        }

        return ris
        }


    fun formatoNomeValido(nome: String): Boolean {
        var ris:Boolean = false
        val regex = Regex("[^a-zA-Z0-9]")

        val caratteri_speciali = regex.containsMatchIn(nome)
        if ( nome.length > 2 && nome.length < 20 ){
            if ( !caratteri_speciali){
                ris = true
            }
        }
        return ris
    }
    fun formatoCognomeValido(cognome: String): Boolean {
        return formatoNomeValido(cognome)
    }
}