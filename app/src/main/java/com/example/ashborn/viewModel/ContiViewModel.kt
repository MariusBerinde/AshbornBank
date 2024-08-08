package com.example.ashborn.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.AskPinViewModel
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.Stato
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.CardRepository
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class ContiViewModel(application: Application): AndroidViewModel(application) {
    val nameClass = AskPinViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val codCliente = run {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }

    private val userRepository: OfflineUserRepository = OfflineUserRepository(ashbornDao)
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)

    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    var operazioniConto by mutableStateOf(arrayListOf<Operation>())
    var contoMostrato by mutableStateOf(
        Conto("xxxx xxx xxx","XXX XXX XXX",0.0, "fdagfkldnaògad", Stato.ATTIVO)
    )

    var listaConti by mutableStateOf(
        arrayListOf<Conto>(
            Conto("777777777777","777 777 777",0.0, "IT1234567890123456789012345", Stato.ATTIVO)
        )
    )
    fun getOperationsConto() {
        Log.d("viewModel","getOpConto nrCarta=${contoMostrato.codConto}, from= ${LocalDateTime.now().minusDays(30)}, upTo=${LocalDateTime.now()}")
        viewModelScope.launch(Dispatchers.IO) {
            val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                return@async operationRepository.getOperazioniConto(contoMostrato.codConto, LocalDateTime.now().minusDays(30), LocalDateTime.now(),0, 10)
            }.await()
            operazioniConto = operazioniDb.first().toCollection(ArrayList<Operation>())
            Log.d("ViewModel", "getOperationConto : $operazioniConto")
        }
    }
    fun getConti(){

        viewModelScope.launch(Dispatchers.IO) {
            var datiDb=viewModelScope.async(Dispatchers.IO) {
                return@async    contoRepository.getConti(codCliente)
            }.await()

            listaConti = datiDb.first().toCollection(ArrayList<Conto>())
            contoMostrato = listaConti.get(0)
            getOperationsConto()
        }

    }
}

@Suppress("UNCHECKED_CAST")
class ContiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContiViewModel::class.java)) {
            return ContiViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}