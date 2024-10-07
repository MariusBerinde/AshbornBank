package com.example.ashborn.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.view.login.StatoErrore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

/**
 * ViewModel for managing bank transfer (bonifico) operations.
 *
 * This ViewModel holds and manages the data required for a bank transfer,
 * including beneficiary details, IBAN, amount, reason, and execution date.
 * It also provides methods to set and validate this data, and interacts with
 * the [ContoRepository] to access account information.
 *
 * @param application The application context. used to get the instance of the database and the datastoremanager
 */
class BonificoViewModel(application: Application): AndroidViewModel(application) {
    private val dsm = DataStoreManager.getInstance(application)
    private val ashbornDao: AshbornDao = AshbornDb.getDatabase(application).ashbornDao()
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)

    /**
     * The client code retrieved from DataStore.
     */
    val codCliente: String = runBlocking { dsm.codClienteFlow.first() }
     /**
     * Error state for the transfer reason (causale).
     */
    var erroreCausale by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the beneficiary name.
     */
    var erroreBeneficiario by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the IBAN.
     */
    var erroreIban by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the transfer amount.
     */
    var erroreImporto by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the execution date.
     */
    var erroreDataAccredito by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * List of accounts associated with the client.
     */
    val listaConti = runBlocking { contoRepository.getConti(codCliente).first() }
    /**
     * Selected account code for the transfer.
     */
    var codConto by mutableStateOf(if(listaConti.isNotEmpty()) listaConti[0].codConto else "")
        private set
    /**
     * Beneficiary name for the transfer.
     */
    var beneficiario by mutableStateOf("")
        private set
    /**
     * IBAN for the transfer.
     */
    var iban by  mutableStateOf("")
        private set
    /**
     * Amount for the transfer.
     */
    var importo by mutableStateOf("")
        private set
    /**
     * Reason for the transfer (causale).
     */
    var causale by mutableStateOf("")
        private set
    /**
     * Execution date for the transfer.
     */
    var dataAccredito: LocalDateTime by mutableStateOf(LocalDateTime.now())
        private set

     /**
     * Sets the selected account code.
     * @param codConto The account code to set.
     */
    fun setCodContoX(codConto: String) {
        this.codConto = codConto
    }

    /**
     * Sets the beneficiary name.
     * @param beneficiario The beneficiary name to set.
     */
    fun setBeneficiarioX(beneficiario: String) {
        this.beneficiario = beneficiario
    }

    /**
     * Sets the IBAN.
     * @param iban The IBAN to set.
     */
    fun setIbanX(iban: String) {
        this.iban = iban
    }

    /**
     * Sets the transfer amount, replacing commas with dots for decimal separation.
     *@param importo The amount to set.
     */
    fun setImportoX(importo: String) {

        this.importo = importo.replace(",",".")
    }

    /**
     * Sets the transfer reason (causale).
     * @param causale The reason to set.
     */
    fun setCausaleX(causale: String) {
        this.causale = causale
    }

    /**
     * Sets the execution date.
     * @param dataAccredito The execution date to set.
     */
    fun setDataAccreditoX(dataAccredito: LocalDateTime) {
        this.dataAccredito = dataAccredito
    }

    /**
     * The following methods set the error states for the corresponding fields.
     * @param statoErrore the error state to set for dataAccredito.
     */
    fun setErroreDataAccreditoX(statoErrore: StatoErrore) {
        this.erroreDataAccredito = statoErrore
    }

    fun setErroreCausaleX(statoErrore: StatoErrore) {
        this.erroreCausale = statoErrore
    }

    fun setErroreImportoX(statoErrore: StatoErrore) {
        this.erroreImporto = statoErrore
    }

    fun setErroreIbanX(statoErrore: StatoErrore) {
        this.erroreIban = statoErrore
    }

    fun setErroreBeneficiarioX(statoErrore: StatoErrore) {
        this.erroreBeneficiario = statoErrore
    }
}

/**
 * Factory for creating [BonificoViewModel] instances.
 *
 * @param application The application context.
 *
 */
@Suppress("UNCHECKED_CAST")
class BonificoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Create
     *
     * @param modelClass the class of the view model to create
     * @return an instance of the view model
     * @throws IllegalArgumentException
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BonificoViewModel::class.java)) {
            return BonificoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}