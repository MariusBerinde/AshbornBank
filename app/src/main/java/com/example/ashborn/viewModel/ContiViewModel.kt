package com.example.ashborn.viewModel

import android.app.Application
import android.graphics.Path.Op
import android.media.VolumeShaper
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
    val nameClass = ContiViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val codCliente = runBlocking {

        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }

    private val userRepository: OfflineUserRepository = OfflineUserRepository(ashbornDao)
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)

    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    private var _listaConti = runBlocking{
        getConti()
    }
    var listaConti by mutableStateOf(
       _listaConti
    )

    var contoMostrato by mutableStateOf(
        if(_listaConti.isEmpty()) null  else _listaConti.get(0)
    )
    private var _operazioniConto = runBlocking{
        //arrayListOf<Operation>()
        contoMostrato?.let {  getOperationsConto(contoMostrato!!)}

    }
    var operazioniConto by mutableStateOf(
        _operazioniConto
    )
    fun getOperationsConto(cm:Conto= //Conto(codConto = "42",codCliente ="777777777", stato = Stato.ATTIVO, iban = "IT1234567890123456789012345",saldo = 190000.00 )
                               Conto("xxxx xxx xxx","XXX XXX XXX",0.0, "fdagfkldnaògad",Stato.ATTIVO)
    ): ArrayList<Operation> {
        val nameFun = nameClass+","+object {}.javaClass.enclosingMethod.name
        var dati = arrayListOf<Operation>();
            Log.d(nameFun,"Conto mostato ${cm?.codConto}")
            runBlocking (Dispatchers.IO) {
                val operazioniDb = viewModelScope.async(Dispatchers.IO) {
                    return@async operationRepository.getOperazioniConto(
                        cm.codConto,
                        LocalDateTime.now().minusDays(30),
                        LocalDateTime.now(),
                        0,
                        10
                    )
                }.await()
                dati = operazioniDb.first().toCollection(ArrayList<Operation>())
           //     Log.d(nameFun, "getOperationConto : $operazioniConto")
            }

        return dati
    }

        fun getConti():ArrayList<Conto>{
            var dati = arrayListOf<Conto>();
            runBlocking (Dispatchers.IO) {
                var datiDb=viewModelScope.async(Dispatchers.IO) {
                    return@async    contoRepository.getConti(codCliente)
                }.await()

                dati=datiDb.first().toCollection(ArrayList<Conto>())

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