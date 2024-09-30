package com.example.ashborn.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.Avviso
import com.example.ashborn.data.StatoAvviso
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.AvvisiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("MutableCollectionMutableState")
class AvvisiViewModel(application: Application): AndroidViewModel(application) {
    val nameClass = AvvisiViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val codCliente = runBlocking {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }
    private var avvisiRepository:AvvisiRepository = AvvisiRepository(ashbornDao)

    private var _listaAvvisi = runBlocking{
        getAvvisi()
    }
    var listaAvvisi by mutableStateOf(
       _listaAvvisi
    )

    fun getAvvisi():ArrayList<Avviso>{
        var dati = arrayListOf<Avviso>()
        runBlocking (Dispatchers.IO) {
            val datiDb = viewModelScope.async(Dispatchers.IO) {
                return@async avvisiRepository.getAvvisi(codCliente)
            }.await()
            dati = datiDb.first().toCollection(ArrayList())
        }
        return dati
    }

    fun deleteAvviso(avviso: Avviso) {
        viewModelScope.launch { avvisiRepository.rimuoviAvviso(avviso) }
    }

    fun aggiornaStatoAvviso(id:Long, nuovoStato:StatoAvviso) {
        viewModelScope.launch(Dispatchers.IO) { avvisiRepository.aggiornaStatoAvviso(id, nuovoStato) }
    }
}

@Suppress("UNCHECKED_CAST")
class AvvisiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AvvisiViewModel::class.java)) {
            return AvvisiViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
