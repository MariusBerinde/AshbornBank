package com.example.ashborn.viewModel
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.repository.OperationRepository

class OperationViewModel(application: Application): AndroidViewModel(application) {
    var ordinanteMav by mutableStateOf("")
        private set
    var codiceMav by mutableStateOf("")
        private set
    var importoMav by mutableStateOf("")
        private set
    var descrizioneMav by mutableStateOf("")
        private set
    var dataAccreditoMav by mutableStateOf("")
        private set
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

    private val ashbornDao: AshbornDao
    private val operationRepository: OperationRepository
    init {
        ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
        operationRepository = OperationRepository(ashbornDao = ashbornDao)
        /*viewModelScope.launch {
            operationRepository.insertOperation(Operation(1, "777777777", LocalDateTime.now(), LocalDateTime.now(), "pagamento crimini di guerra", 135.89, TransactionType.WITHDRAWAL))
        }*/
    }

    fun setCodContoX(codConto: String) { this.codConto = codConto}
    fun setBeneficiarioX(beneficiario: String) { this.beneficiario = beneficiario}
    fun setIbanX(iban: String) { this.iban = iban}
    fun setImportoX(importo: String) { this.importo = importo.replace(",",".")}
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

    fun setDataAccreditoMavX(dataAccreditoMav: String) {
        this.dataAccreditoMav = dataAccreditoMav
    }

    fun setDescrizioneMavX(descrizioneMav: String) {
        this.descrizioneMav = descrizioneMav
    }

    fun setImportoMavX(importoMav: String) {
        this.importoMav = importoMav
    }

    fun setCodiceMavX(codiceMav: String) {
        this.codiceMav = codiceMav
    }

    fun setOrdinanteMavX(ordinanteMav: String) {
        this.ordinanteMav = ordinanteMav
    }
}

@Suppress("UNCHECKED_CAST")
class OperationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationViewModel::class.java)) {
            return OperationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}