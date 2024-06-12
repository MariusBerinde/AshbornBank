package com.example.ashborn.viewModel
import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.*
import kotlinx.datetime.format.*

class OperationViewModel: ViewModel() {
    var erroreDataAccredito by mutableIntStateOf(0)
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification
    var startDest: String = ""
        private set
    var codConto: String = "0987654321"
        private set
    var beneficiario by mutableStateOf("")
        private set
    var iban by  mutableStateOf("")
        private set
    var importo by mutableStateOf("")
        private set
    var causale by mutableStateOf("")
        private set
    var dataAccredito by mutableStateOf("")
        private set
    var pin by mutableStateOf((""))
        private set // Optional: restrict external modification


    fun setCodContoX(codConto: String) { this.codConto = codConto}
    fun setBeneficiarioX(beneficiario: String) { this.beneficiario = beneficiario}
    fun setIbanX(iban: String) { this.iban = iban}
    fun setImportoX(importo: String) { this.importo = importo}
    fun setCausaleX(causale: String) { this.causale = causale}
    fun setDataAccreditoX(dataAccredito: String) { this.dataAccredito = dataAccredito}
    fun setPinX(pin: String) { this.pin = pin }
    fun set_StartDest(startDest: String){
        if (startDest == "init" || startDest == "principale") {
            this.startDest= startDest
        }
    }
    fun checkPin(): Boolean { return false }

    fun incrementWrongAttempts() { this.wrongAttempts++ }


    /**@param beneficiario il nome e il cognome del beneficiario
     * @param iban il codice iban del beneficiario
     * @param importo l'importo del bonifico in formato stringa
     * @param causale la motivazione del bonifico
     * @param dataAccredito la data in cui verrà effettuato il bonifico
     * @return true se il bonifico ha dati validi per effettuarlo false altrimenti
     */
    fun formatoBonificoValido(beneficiario: String, iban: String, importo: String, causale: String, dataAccredito: String): Boolean {
        val beneficiarioOK: Boolean = formatoBeneficiarioValido(beneficiario)
        val ibanOK: Boolean = formatoIbanValido(iban)
        val importoOK: Boolean = formatoImportoValido(importo)
        val causaleOK: Boolean = formatoCausaleValida(causale)
        val dataAccreditoOK: Boolean = formatoDataAccreditoValida(dataAccredito)
        return beneficiarioOK && ibanOK && importoOK && causaleOK && dataAccreditoOK
    }

    /**
     * @param beneficiario il beneficiario del bonifico deve contenere nome e cognome del beneficiario o il nome di un'azienda
     * @return true se il beneficiario è valido false altrimenti
     */
    fun formatoBeneficiarioValido(beneficiario: String): Boolean {
        return true
    }

    /**
     * @param iban l'iban da controllare
     * @return true se l'iban è valido false altrimenti
     */
    fun formatoIbanValido(iban: String): Boolean {
        return true
    }

    /**
     * @param causale la causale del bonifico da effettuare
     * @return true se la causale è valida false altrimenti
     */
    fun formatoCausaleValida(causale: String): Boolean {
        return true
    }

    /**
     * @param dataAccredito la data in cui verrà effettuato il bonifico
     * @return true se la data è valida false altrimenti
     */
    fun formatoDataAccreditoValida(dataAccredito: String): Boolean {
        var ris:Boolean = false
        val pattern= "\\d{2}-\\d{2}-\\d{4}||\\d{2}/\\d{2}/\\d{4}"
        if (Regex(pattern).matches(dataAccredito)){
            val day=dataAccredito.substring(0,2).toInt()
            val month=dataAccredito.substring(3,5).toInt()
            val year=dataAccredito.substring(6,dataAccredito.length).toInt()
            var dataEsistente: Boolean = false
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
                val dataAccreditoData = LocalDate(year, month, day)
                val oggi = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                if(dataAccreditoData >= oggi && dataAccreditoData <= oggi.plus(6, DateTimeUnit.MONTH)){
                    ris = true
                }
            }
        }
        return ris
    }

    /**
     * @param importo l'importo del bonifico da effettuare
     * @return true se l'importo è valido false altrimenti
     */
    fun formatoImportoValido(importo: String): Boolean {
        var ris:Boolean = false
        val pattern= "\\d{1,5}\\.\\d{0,2}"

        if (Regex(pattern).matches(importo)){
            ris = (importo.toDouble() < 15000.0)
        }
        return ris
    }

    fun setErroreDataAccreditoX(b: Boolean) {
        this.erroreDataAccredito = if (b) {1} else {0}
    }

    fun setErroreCausaleX(b: Boolean) {

    }

    fun setErroreImportoX(b: Boolean) {

    }

    fun setErroreIbanX(b: Boolean) {

    }

    fun setErroreBeneficiarioX(b: Boolean) {

    }
}