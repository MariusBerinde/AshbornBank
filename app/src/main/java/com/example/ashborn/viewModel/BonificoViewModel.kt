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
import com.example.ashborn.repository.OperationRepository
import com.example.ashborn.view.login.StatoErrore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class BonificoViewModel(application: Application): AndroidViewModel(application) {
    private val dsm = DataStoreManager.getInstance(application)
    private val ashbornDao: AshbornDao = AshbornDb.getDatabase(application).ashbornDao()
    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)

    val codCliente: String = runBlocking { dsm.codClienteFlow.first() }
    var erroreCausale by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreBeneficiario by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreIban by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreImporto by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreDataAccredito by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var startDest: String = ""
        private set

    val listaConti = runBlocking { contoRepository.getConti(codCliente).first() }
    var codConto by mutableStateOf(if(!listaConti.isEmpty()) listaConti.get(0).codConto else "")

        private set
    var beneficiario by mutableStateOf("")
        private set
    var iban by  mutableStateOf("")
        private set
    var importo by mutableStateOf("")
        private set
    var causale by mutableStateOf("")
        private set
    var dataAccredito by mutableStateOf(LocalDateTime.now())
        private set

    fun setCodContoX(codConto: String) {
        this.codConto = codConto
    }

    fun setBeneficiarioX(beneficiario: String) {
        this.beneficiario = beneficiario
    }

    fun setIbanX(iban: String) {
        this.iban = iban
    }

    fun setImportoX(importo: String) {

        this.importo = importo.replace(",",".")
    }

    fun setCausaleX(causale: String) {
        this.causale = causale
    }

    fun setDataAccreditoX(dataAccredito: LocalDateTime) {
        this.dataAccredito = dataAccredito
    }

    fun set_StartDest(startDest: String) {
        if (startDest == "init" || startDest == "principale") {
            this.startDest = startDest
        }
    }

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

@Suppress("UNCHECKED_CAST")
class BonificoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BonificoViewModel::class.java)) {
            return BonificoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}