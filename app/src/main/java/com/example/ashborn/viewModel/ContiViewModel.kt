package com.example.ashborn.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.Stato
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

@SuppressLint("MutableCollectionMutableState")
class ContiViewModel(application: Application): AndroidViewModel(application) {
    private val nameClass = ContiViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val codCliente = runBlocking {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }

    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)

    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    private var _listaConti = runBlocking{ getConti() }
    private var listaConti by mutableStateOf(_listaConti)
    private var indiceContoMostrato by mutableIntStateOf(0)
    var contoMostrato by mutableStateOf(
        if(_listaConti.isEmpty()) null  else _listaConti[indiceContoMostrato]
    )
    private var _operazioniConto = runBlocking{
        contoMostrato?.let {  getOperationsConto(contoMostrato!!)}
    }
    var operazioni by mutableStateOf(_operazioniConto)
    fun contoPrecedente() {
        indiceContoMostrato = if (indiceContoMostrato > 0) indiceContoMostrato - 1 else listaConti.size - 1
        contoMostrato = listaConti[indiceContoMostrato]
        operazioni = runBlocking{ contoMostrato?.let {  getOperationsConto(contoMostrato!!) } }
    }
    fun contoSuccessivo() {
        indiceContoMostrato = if (indiceContoMostrato < listaConti.size - 1) indiceContoMostrato + 1 else 0
        contoMostrato = listaConti[indiceContoMostrato]
        operazioni = runBlocking{ contoMostrato?.let {  getOperationsConto(contoMostrato!!) } }
    }
    private fun getOperationsConto(
        cm: Conto = Conto("","",0.0, "", Stato.ATTIVO)
    ): ArrayList<Operation> {
        val nameFun = nameClass + "," + (object {}.javaClass.enclosingMethod?.name ?: "")
        var dati = arrayListOf<Operation>()
        Log.d(nameFun,"Conto mostato ${cm.codConto}")
        runBlocking (Dispatchers.IO) {
            val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                return@async operationRepository.getOperazioniConto(
                    cm.codConto,
                    LocalDateTime.now().minusDays(30),
                    LocalDateTime.now().plusMonths(18),
                    0,
                    10
                )
            }.await()
            dati = operazioniDb.first().toCollection(ArrayList())
        }
        return dati
    }

    private fun getConti(): ArrayList<Conto> {
        var dati = arrayListOf<Conto>()
        runBlocking (Dispatchers.IO) {
            val datiDb = viewModelScope.async(Dispatchers.IO) {
                return@async contoRepository.getConti(codCliente)
            }.await()
            dati = datiDb.first().toCollection(ArrayList())
        }
        return dati
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