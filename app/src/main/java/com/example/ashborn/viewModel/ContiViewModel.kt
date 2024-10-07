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
/**
 * ContiViewModel is responsible for managing the user's accounts and operations.
 *
 * This ViewModel handles fetching, displaying, and navigating through the user's
 * accounts (conti) and the associated operations for each account. It interacts
 * with the `DataStoreManager`, `ContoRepository`, and `OperationRepository` to retrieve
 * and update data.
 *
 * @property application The application instance used for accessing context and data resources.
 */
@SuppressLint("MutableCollectionMutableState")
class ContiViewModel(application: Application): AndroidViewModel(application) {
    /**
     * Name of the current class used in logging for tracking operations.
     */
    private val nameClass = ContiViewModel::class.simpleName
    /**
     * Instance of `DataStoreManager` for managing user-related data.
     */
    val dsm = DataStoreManager.getInstance(application)
    /**
     * Data Access Object (DAO) for interacting with the Ashborn database.
     */
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    /**
     * Client code (codCliente) retrieved from `DataStore` for the current user.
     */
    val codCliente = runBlocking {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }
    /**
     * Repository for managing accounts (conti) data.
     */
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)
    /**
     * Repository for managing operations data related to an account.
     */
    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    /**
     * List of accounts for the current user, initialized using a blocking call to retrieve data.
     */
    private var _listaConti = runBlocking{ getConti() }
    private var listaConti by mutableStateOf(_listaConti)
    /**
     * Index of the currently displayed account.
     */
    private var indiceContoMostrato by mutableIntStateOf(0)
    /**
     * The currently displayed account (conto).
     */
    var contoMostrato by mutableStateOf(
        if(_listaConti.isEmpty()) null  else _listaConti[indiceContoMostrato]
    )
    /**
     * List of operations for the currently displayed account.
     * Initialized by fetching operations for the displayed account.
     */
    private var _operazioniConto = runBlocking{
        contoMostrato?.let {  getOperationsConto(contoMostrato!!)}
    }

    var operazioni by mutableStateOf(_operazioniConto)
    /**
     * Displays the previous account (conto) and updates the list of operations.
     */
    fun contoPrecedente() {
        indiceContoMostrato = if (indiceContoMostrato > 0) indiceContoMostrato - 1 else listaConti.size - 1
        contoMostrato = listaConti[indiceContoMostrato]
        operazioni = runBlocking{ contoMostrato?.let {  getOperationsConto(contoMostrato!!) } }
    }
    /**
     * Displays the next account (conto) and updates the list of operations.
     */
    fun contoSuccessivo() {
        indiceContoMostrato = if (indiceContoMostrato < listaConti.size - 1) indiceContoMostrato + 1 else 0
        contoMostrato = listaConti[indiceContoMostrato]
        operazioni = runBlocking{ contoMostrato?.let {  getOperationsConto(contoMostrato!!) } }
    }
    /**
     * Fetches the operations related to the currently displayed account (conto).
     *
     * @param cm The account (conto) for which operations are being retrieved.
     * @return List of operations (ArrayList of `Operation`).
     */
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
    /**
     * Fetches the list of accounts (conti) for the current user.
     *
     * @return List of accounts (ArrayList of `Conto`).
     */
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
/**
 * Factory class for creating an instance of `ContiViewModel`.
 *
 * This factory is required when the `ViewModel` requires an `Application` instance
 * in its constructor. It is used to instantiate `ContiViewModel` within the `ViewModelProvider`.
 *
 * @property application The application instance passed to the ViewModel.
 */
@Suppress("UNCHECKED_CAST")
class ContiViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Creates an instance of `ContiViewModel` if the requested class matches.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return The instance of `ContiViewModel`.
     * @throws IllegalArgumentException If the requested class is not `ContiViewModel`.
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContiViewModel::class.java)) {
            return ContiViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}