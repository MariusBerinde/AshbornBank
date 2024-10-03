package com.example.ashborn.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.BarcodeScanner
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.view.login.StatoErrore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
class MavViewModel  (application: Application): AndroidViewModel(application) {
    private val className = MavViewModel::class.simpleName
    val barcodeScanner = BarcodeScanner( application )
    var codiceMav by mutableStateOf("")
        private set
    var importoMav by mutableStateOf("")
        private set
    var descrizioneMav by mutableStateOf("")
        private set
    var dataAccreditoMav: LocalDateTime by mutableStateOf(LocalDateTime.now())
        private set
    private val dsm = DataStoreManager.getInstance(application)
    private val ashbornDao: AshbornDao = AshbornDb.getDatabase(application).ashbornDao()
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)
    val codCliente: String = runBlocking { dsm.codClienteFlow.first() }
    var erroreCodiceMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreDescrizioneMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    var erroreImportoMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    private var erroreDataAccreditoMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    val listaConti = runBlocking { contoRepository.getConti(codCliente).first() }
    var codConto by mutableStateOf(if(listaConti.isNotEmpty()) listaConti[0].codConto else "")
        private set
    var ordinanteMav by mutableStateOf(if(listaConti.isNotEmpty()) listaConti[0].codConto else "")
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

    fun validateMav(): Boolean {
        val reg = Regex("[a-zA-Z0-9]+")
        val reg2 = Regex("\\d{1,5}(,|.)?\\d{0,2}")
        Log.d(className, """
            codice mav corretto: ${reg.matches(codiceMav)} ${codiceMav.isNotBlank()}
            importo mav corretto: ${reg.matches(importoMav)} ${importoMav.isNotBlank()}
            descrizione mav corretta: ${reg.matches(descrizioneMav)} ${descrizioneMav.isNotBlank()}           
        """.trimMargin())
        erroreCodiceMav = if(codiceMav.isNotBlank() && reg.matches(codiceMav))
                              StatoErrore.NESSUNO
                          else
                              StatoErrore.FORMATO
        erroreImportoMav = if(importoMav.isNotBlank() && reg2.matches(importoMav))
                               StatoErrore.NESSUNO
                           else
                               StatoErrore.FORMATO
        erroreDescrizioneMav = if(descrizioneMav.isNotBlank() && reg.matches(descrizioneMav))
                                   StatoErrore.NESSUNO
                               else
                                   StatoErrore.FORMATO

        return  erroreCodiceMav == StatoErrore.NESSUNO &&
                erroreImportoMav == StatoErrore.NESSUNO &&
                erroreDataAccreditoMav == StatoErrore.NESSUNO
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