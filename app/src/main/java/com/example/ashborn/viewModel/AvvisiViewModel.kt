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

/**
 * ViewModel for managing user notifications -used for the composables Avvisi and DettagliAvviso.
 *
 * This ViewModel provides functionality to retrieve, delete, and update the status of
 * notifications for a specific user. It interacts with the [AvvisiRepository] to
 * access and modify notification data in the database.
 *
 * @param application The application context. Used for getting instances of database and datastoremanager
 */
@SuppressLint("MutableCollectionMutableState")
class AvvisiViewModel(application: Application): AndroidViewModel(application) {
    val nameClass = AvvisiViewModel::class.simpleName
    val dsm = DataStoreManager.getInstance(application)
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    /**
     * The client code retrieved from DataStore.
     */
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
    /**
     * List of notifications for the user.
     */
    var listaAvvisi by mutableStateOf(
       _listaAvvisi
    )

    private fun getAvvisi():ArrayList<Avviso>{
        var dati = arrayListOf<Avviso>()
        runBlocking (Dispatchers.IO) {
            val datiDb = viewModelScope.async(Dispatchers.IO) {
                return@async avvisiRepository.getAvvisi(codCliente)
            }.await()
            dati = datiDb.first().toCollection(ArrayList())
        }
        return dati
    }

    /**
     * Deletes a notification.
     *
     * @param avviso The notification to delete.
     */
    fun deleteAvviso(avviso: Avviso) {
        viewModelScope.launch { avvisiRepository.rimuoviAvviso(avviso) }
    }

    /**
     * Updates the status of a notification. mainly used to set the status of the notification to read
     *
     * @param id The ID of the notification to update.
     * @param nuovoStato The new status for the notification.
     */
    fun aggiornaStatoAvviso(id:Long, nuovoStato:StatoAvviso) {
        viewModelScope.launch(Dispatchers.IO) { avvisiRepository.aggiornaStatoAvviso(id, nuovoStato) }
    }
}

/**
 * Factory for creating [AvvisiViewModel] instances.
 *
 * @param application The application context.
 */
@Suppress("UNCHECKED_CAST")
class AvvisiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Create
     *
     * @param modelClass the class of the view model to create
     * @return an instance of the view model
     * @throws IllegalArgumentException
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AvvisiViewModel::class.java)) {
            return AvvisiViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
