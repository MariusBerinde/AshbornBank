package com.example.ashborn.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime

open class AshbornViewModel: ViewModel() {
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
    /**
     * Controlla il formato di dataNascita , il formato è considerato valido se rispetta il pattern giorno/mese/anno o giorno-mese-anno
     * @param dataNascita : la data di nascita del cliente
     * @return true se data di nascita ha un formato valido
     */
    fun formatoDataNascitaValida(dataNascita: String): Boolean{
        var ris:Boolean = false
        if (dataNascita.isEmpty()) return ris
        val pattern= "\\d{1,2}-\\d{1,2}-\\d{4}||\\d{1,2}/\\d{1,2}/\\d{4}"
        if (Regex(pattern).matches(dataNascita)){
            val campiData: List<String> = dataNascita.replace("-","/").split("/")
            val day = campiData[0].toInt()
            val month = campiData[1].toInt()
            val year = campiData[2].toInt()
            var dataEsistente = false
            when(month){
                1,3,5,7,8,10,12 -> dataEsistente = if(day > 0 && day <= 31) true
                else false
                4,6,9,11 -> dataEsistente = if(day > 0 && day <= 30) true
                else false
                2 -> if( (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
                    dataEsistente = if(day > 0 && day <= 29) true
                    else false
                } else {
                    dataEsistente = if(day > 0 && day <= 28) true
                    else false
                }
                else -> dataEsistente = false
            }

            if(dataEsistente) {
                val oggi = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val maggiorenne = LocalDate(year, month, day) <= oggi.minus(18, DateTimeUnit.YEAR)
                if (maggiorenne)
                    ris = true
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
//Todo: fai in modo che prenda gli spazi se c'è un doppio nome
        val caratteri_speciali = regex.containsMatchIn(nome)
        if ( nome.length >= 2 && nome.length <= 20 ){
            if ( !caratteri_speciali){
                ris = true
            }
        }
        return ris
    }
    fun formatoCognomeValido(cognome: String): Boolean {
        return formatoNomeValido(cognome)
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
}