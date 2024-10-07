package com.example.ashborn.viewModel
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
/**
 * OperationViewModel is a ViewModel that manages the state of a financial operation,
 * including validation errors for different fields like IBAN, amount, beneficiary, etc.
 *
 * This ViewModel uses Jetpack Compose's `mutableStateOf` and `mutableIntStateOf` to track
 * the state of errors and input fields related to the operation, ensuring reactive updates
 * to the UI whenever these states change.
 *
 * @property application The application instance used to access context if needed.
 */
class OperationViewModel(application: Application): AndroidViewModel(application) {
    /**
     * Error state for the 'causale' field (cause or reason for the transaction).
     * A value of `1` indicates an error, and `0` means no error.
     */
    var erroreCausale by mutableIntStateOf(0)
        private set
    /**
     * Error state for the 'beneficiario' field (the beneficiary of the transaction).
     * A value of `1` indicates an error, and `0` means no error.
     */
    var erroreBeneficiario by mutableIntStateOf(0)
        private set
    /**
     * Error state for the 'iban' field (the IBAN of the transaction).
     * A value of `1` indicates an error, and `0` means no error.
     */
    var erroreIban by mutableIntStateOf(0)
        private set
    /**
     * Error state for the 'importo' field (the amount of the transaction).
     * A value of `1` indicates an error, and `0` means no error.
     */
    var erroreImporto by mutableIntStateOf(0)
        private set
    /**
     * Error state for the 'data accredito' field (the credit date of the transaction).
     * A value of `1` indicates an error, and `0` means no error.
     */
    var erroreDataAccredito by mutableIntStateOf(0)
        private set
    /**
     * Account code associated with the operation.
     * It is initialized with a default value and is private to prevent external modification.
     */
    var codConto: String = "0987654321"
        private set
    /**
     * IBAN associated with the operation. Initially empty and private to prevent external modification.
     */
    var iban by  mutableStateOf("")
        private set
    /**
     * Amount of the transaction. Initially empty and private to prevent external modification.
     */
    var importo by mutableStateOf("")
        private set
    /**
     * PIN associated with the operation. Initially empty and private to prevent external modification.
     */
    var pin by mutableStateOf((""))
        private set

    fun setErroreDataAccreditoX(b: Boolean) {
        this.erroreDataAccredito = if (b) {1} else {0}
    }

    /**
     * Sets the error state for the 'causale' field.
     *
     * @param hasError Boolean indicating if there's an error (true) or not (false).
     */
    fun setErroreCausaleX(b: Boolean) {
        this.erroreCausale = if (b) {1} else {0}
    }


    /**
     * Sets the error state for the 'importo' field.
     *
     * @param hasError Boolean indicating if there's an error (true) or not (false).
     */
    fun setErroreImportoX(b: Boolean) {
        this.erroreImporto = if (b) {1} else {0}
    }

    /**
     * Sets the error state for the 'iban' field.
     *
     * @param hasError Boolean indicating if there's an error (true) or not (false).
     */
    fun setErroreIbanX(b: Boolean) {
        this.erroreIban = if (b) {1} else {0}

    }

    /**
     * Sets the error state for the 'beneficiario' field.
     *
     * @param hasError Boolean indicating if there's an error (true) or not (false).
     */
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