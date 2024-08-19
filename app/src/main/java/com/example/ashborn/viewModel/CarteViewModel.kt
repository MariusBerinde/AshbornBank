package com.example.ashborn.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Operation
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.CardRepository
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class CarteViewModel(application: Application): AndroidViewModel(application) {
    val networkConnectivityObserver = NetworkConnectivityObserver.getInstance(application)
    val nameClass = CarteViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val codCliente = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }

    private val userRepository: OfflineUserRepository = OfflineUserRepository(ashbornDao)
    private val cardRepository: CardRepository = CardRepository(ashbornDao)

    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    private var _listaCarte = runBlocking{ getCarte() }
    var listaCarte by mutableStateOf(_listaCarte)

    var cartaMostrata by mutableStateOf(
        if(_listaCarte.isEmpty()) null  else _listaCarte.get(0)
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
        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.cognomeFlow.first().also { ris = it }
        }
    }

    fun getOperationsCarta(cm: Carta): ArrayList<Operation> {
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
            dati = operazioniDb.first().toCollection(ArrayList<Operation>())
            Log.d(nameFun, "Operazioni Carta : $operazioniDb")
        }

        return dati
    }

    fun getCarte() : ArrayList<Carta> {
        var dati = arrayListOf<Carta>()
        runBlocking (Dispatchers.IO) {
            val datiDb = viewModelScope.async(Dispatchers.IO) {
                return@async cardRepository.getCarte(codCliente)
            }.await()

            dati = datiDb.first().toCollection(ArrayList<Carta>())

        }
        return dati
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