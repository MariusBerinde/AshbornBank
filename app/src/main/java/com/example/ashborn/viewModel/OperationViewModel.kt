package com.example.ashborn.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OperationViewModel: ViewModel() {
    var erroreCausale by mutableIntStateOf(0)
        private set
    var erroreBeneficiario by mutableIntStateOf(0)
        private set
    var erroreIban by mutableIntStateOf(0)
        private set
    var erroreImporto by mutableIntStateOf(0)
        private set
    var erroreDataAccredito by mutableIntStateOf(0)
        private set
//    var errore by mutableIntStateOf(0)
//        private set
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




    fun setErroreDataAccreditoX(b: Boolean) {
        this.erroreDataAccredito = if (b) {1} else {0}
    }

    fun setErroreCausaleX(b: Boolean) {
        this.erroreCausale = if (b) {1} else {0}
    }

    fun setErroreImportoX(b: Boolean) {
        this.erroreImporto = if (b) {1} else {0}

    }

    fun setErroreIbanX(b: Boolean) {
        this.erroreIban = if (b) {1} else {0}

    }

    fun setErroreBeneficiarioX(b: Boolean) {
        this.erroreBeneficiario = if (b) {1} else {0}

    }

    fun bloccaUtente() {
        TODO("Not yet implemented")
    }
}