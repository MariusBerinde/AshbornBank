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

/**
 * ViewModel for managing and displaying a list of cards (Carte) and their related operations.
 *
 * This ViewModel handles the fetching of user card data, the retrieval of card-specific operations,
 * and the logic for displaying the next or previous card in the list. It also retrieves user-specific
 * data like the client code, username, and surname from DataStore.
 *
 * @param application The application context, used for initializing the database, repositories,
 * and [DataStoreManager].
 */
@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@SuppressLint("MutableCollectionMutableState")
class CarteViewModel(application: Application): AndroidViewModel(application) {
    // Logging tag for this ViewModel class
    private val nameClass = CarteViewModel::class.simpleName
    // DataStoreManager instance for accessing stored user preferences
    val dsm = DataStoreManager.getInstance(application)
    // DAO and repositories for fetching cards and operations
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    // User client code, username, and surname retrieved from DataStore
    val codCliente = runBlocking {
        var ris: String
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }
    // Current index of the displayed card
    private var indiceCartaMostrata by mutableIntStateOf(0)
    private val cardRepository: CardRepository = CardRepository(ashbornDao)
    private val operationRepository: OperationRepository = OperationRepository(ashbornDao)
    // List of cards and the currently displayed card
    private var _listaCarte = runBlocking{ getCarte() }
    private var listaCarte by mutableStateOf(_listaCarte)
    var cartaMostrata by mutableStateOf(
        if(_listaCarte.isEmpty()) null  else _listaCarte[indiceCartaMostrata]
    )
    // Operations related to the currently displayed card
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
    /**
     * Fetches operations related to a specific card (Carta) from the database.
     *
     * @param cm The card (Carta) for which to retrieve the operations.
     * @return A list of [Operation] objects for the given card.
     */
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
    /**
     * Fetches the list of cards (Carte) associated with the current client from the database.
     *
     * @return A list of [Carta] objects for the user.
     */
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
    /**
     * Switches to the next card in the list. If the last card is displayed, it loops back to the first one.
     */
    fun cartaSuccessiva() {
        indiceCartaMostrata = if (indiceCartaMostrata > 0) indiceCartaMostrata - 1 else listaCarte.size - 1
        cartaMostrata = listaCarte[indiceCartaMostrata]
        operazioni = runBlocking{ cartaMostrata?.let {  getOperationsCarta(cartaMostrata!!) } }
    }
    /**
     * Switches to the previous card in the list. If the first card is displayed, it loops to the last one.
     */
    fun cartaPrecedente() {
        indiceCartaMostrata = if (indiceCartaMostrata < listaCarte.size - 1) indiceCartaMostrata + 1 else 0
        cartaMostrata = listaCarte[indiceCartaMostrata]
        operazioni = runBlocking{ cartaMostrata?.let {  getOperationsCarta(cartaMostrata!!) } }
    }

}

/**
 * Factory class for creating instances of [CarteViewModel].
 *
 * This factory is necessary to provide the [Application] context to the ViewModel.
 *
 * @param application The application context.
 */
@Suppress("UNCHECKED_CAST")
class CarteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Create
     *
     * @param modelClass the class of the view model to create
     * @return an instance of the view model
     * @throws IllegalArgumentException
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarteViewModel::class.java)) {
            return CarteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}