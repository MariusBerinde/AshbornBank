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
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Operation
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.CardRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@SuppressLint("MutableCollectionMutableState")
class CarteViewModel(application: Application): AndroidViewModel(application) {
    private val nameClass = CarteViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val codCliente = runBlocking {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }

    private var indiceCartaMostrata by mutableIntStateOf(0)
    private val cardRepository: CardRepository = CardRepository(ashbornDao)
    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    private var _listaCarte = runBlocking{ getCarte() }
    private var listaCarte by mutableStateOf(_listaCarte)

    var cartaMostrata by mutableStateOf(
        if(_listaCarte.isEmpty()) null  else _listaCarte[indiceCartaMostrata]
    )
    private var _operazioniCarta = runBlocking{
        cartaMostrata?.let { getOperationsCarta(cartaMostrata!!) }
    }
    var operazioni by mutableStateOf(_operazioniCarta)
    val userName = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.usernameFlow.first().also { ris = it }
        }
    }
    val cognome = runBlocking {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.cognomeFlow.first().also { ris = it }
        }
    }

    private fun getOperationsCarta(cm: Carta): ArrayList<Operation> {
        val nameFun = nameClass+","+ (object {}.javaClass.enclosingMethod?.name ?: "")
        var dati = arrayListOf<Operation>()
        //Log.d(nameFun,"Conto mostato ${cm?.codConto}")
        runBlocking (Dispatchers.IO) {
            val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                return@async operationRepository.getOperazioniCarta(
                    cm.nrCarta,
                    LocalDateTime.now().minusDays(30),
                    LocalDateTime.now(),
                    0,
                    10
                )
            }.await()
            dati = operazioniDb.first().toCollection(ArrayList())
            Log.d(nameFun, "Operazioni Carta : $operazioniDb")
        }

        return dati
    }

    private fun getCarte() : ArrayList<Carta> {
        var dati = arrayListOf<Carta>()
        runBlocking (Dispatchers.IO) {
            val datiDb = viewModelScope.async(Dispatchers.IO) {
                return@async cardRepository.getCarte(codCliente)
            }.await()

            dati = datiDb.first().toCollection(ArrayList())

        }
        return dati
    }

    fun cartaSuccessiva() {
        indiceCartaMostrata = if (indiceCartaMostrata > 0) indiceCartaMostrata - 1 else listaCarte.size - 1
        cartaMostrata = listaCarte[indiceCartaMostrata]
        operazioni = runBlocking{ cartaMostrata?.let {  getOperationsCarta(cartaMostrata!!) } }
    }

    fun cartaPrecedente() {
        indiceCartaMostrata = if (indiceCartaMostrata < listaCarte.size - 1) indiceCartaMostrata + 1 else 0
        cartaMostrata = listaCarte[indiceCartaMostrata]
        operazioni = runBlocking{ cartaMostrata?.let {  getOperationsCarta(cartaMostrata!!) } }
    }

}

@Suppress("UNCHECKED_CAST")
class CarteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarteViewModel::class.java)) {
            return CarteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}