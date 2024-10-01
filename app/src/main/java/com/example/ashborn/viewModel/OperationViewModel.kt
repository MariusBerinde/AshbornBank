package com.example.ashborn.viewModel
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

class OperationViewModel(application: Application): AndroidViewModel(application) {
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
    var codConto: String = "0987654321"
        private set
    var iban by  mutableStateOf("")
        private set
    var importo by mutableStateOf("")
        private set
    var pin by mutableStateOf((""))
        private set

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