package com.example.ashborn.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.BarcodeScanner
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.repository.OperationRepository
import com.example.ashborn.view.login.StatoErrore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
class MavViewModel  (
    application: Application,
): AndroidViewModel(application) {
    val barcodeScanner = BarcodeScanner( application )

    var codiceMav by mutableStateOf("")
        private set
    var importoMav by mutableStateOf("")
        private set
    var descrizioneMav by mutableStateOf("")
        private set
    var dataAccreditoMav by mutableStateOf(LocalDateTime.now())
        private set

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

    var ordinanteMav by mutableStateOf(if(!listaConti.isEmpty()) listaConti.get(0).codConto else "")
        private set

    fun setDescrizioneMavX(descrizioneMav: String) {
        this.descrizioneMav = descrizioneMav
    }

    fun setImportoMavX(importoMav: String) {
        this.importoMav = importoMav.replace(",",".")
    }

    fun setCodiceMavX(codiceMav: String) {
        this.codiceMav = codiceMav
    }

    fun setOrdinanteMavX(ordinanteMav: String) {
        this.ordinanteMav = ordinanteMav
    }
   fun setDataAccreditoMavX(dataAccredito:LocalDateTime) {
      this.dataAccreditoMav = dataAccredito
   }

    fun startScan(){
        viewModelScope.launch (Dispatchers.Default){
            barcodeScanner.startScan()
        }
    }

    var barcodeResults = barcodeScanner.barCodeResults
}

@Suppress("UNCHECKED_CAST")
class MavViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MavViewModel::class.java)) {
            return MavViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}