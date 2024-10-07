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
/**
 * MavViewModel is responsible for managing the state and validation of MAV operations.
 *
 * This ViewModel handles the MAV-related data, such as the MAV code, amount, description,
 * and credit date. It also interacts with the local database via the repository pattern
 * to retrieve accounts (conti) and manages error states for the different fields.
 *
 * @property application The application instance used for context and accessing resources like DataStore.
 */
class MavViewModel  (application: Application): AndroidViewModel(application) {
    /**
     * The name of the current class. Used in logging for identifying operations.
     */
    private val className = MavViewModel::class.simpleName
    /**
     * Instance of `BarcodeScanner` used for scanning MAV-related barcodes.
     */
    val barcodeScanner = BarcodeScanner( application )
    /**
     * MAV code entered by the user.
     * This field is private and can only be updated via the setter method.
     */
    var codiceMav by mutableStateOf("")
        private set
    /**
     * MAV amount entered by the user.
     * This field is private and can only be updated via the setter method.
     */
    var importoMav by mutableStateOf("")
        private set
    /**
     * MAV description entered by the user.
     * This field is private and can only be updated via the setter method.
     */
    var descrizioneMav by mutableStateOf("")
        private set

    var dataAccreditoMav: LocalDateTime by mutableStateOf(LocalDateTime.now())
        private set
    /**
     * Instance of `DataStoreManager` for managing user preferences and other data.
     */
    private val dsm = DataStoreManager.getInstance(application)
    /**
     * DAO for accessing the local database (Ashborn).
     */
    private val ashbornDao: AshbornDao = AshbornDb.getDatabase(application).ashbornDao()
    /**
     * Repository for managing account-related operations.
     */
    private val contoRepository: ContoRepository = ContoRepository(ashbornDao)
    /**
     * Client code retrieved from the DataStore.
     * This is fetched using a blocking coroutine.
     */
    val codCliente: String = runBlocking { dsm.codClienteFlow.first() }
    /**
     * Error state for the MAV code field.
     * Default is `StatoErrore.NESSUNO` and can only be updated via validation methods.
     */
    var erroreCodiceMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the MAV description field.
     * Default is `StatoErrore.NESSUNO` and can only be updated via validation methods.
     */
    var erroreDescrizioneMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the MAV amount field.
     * Default is `StatoErrore.NESSUNO` and can only be updated via validation methods.
     */
    var erroreImportoMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * Error state for the MAV credit date field.
     * Default is `StatoErrore.NESSUNO` and is private.
     */
    private var erroreDataAccreditoMav by mutableStateOf(StatoErrore.NESSUNO)
        private set
    /**
     * List of accounts (conti) retrieved from the repository based on the client code.
     * Fetched using a blocking coroutine.
     */
    val listaConti = runBlocking { contoRepository.getConti(codCliente).first() }
    /**
     * Account code for the MAV operation, initialized to the first account if available.
     */
    var codConto by mutableStateOf(if(listaConti.isNotEmpty()) listaConti[0].codConto else "")
        private set
    /**
     * The MAV sender (ordinante) account, initialized to the first account if available.
     */
    var ordinanteMav by mutableStateOf(if(listaConti.isNotEmpty()) listaConti[0].codConto else "")
        private set

    /**
     * Sets the MAV description.
     *
     * @param descrizioneMav The MAV description entered by the user.
     */
    fun setDescrizioneMavX(descrizioneMav: String) {
        this.descrizioneMav = descrizioneMav
    }
    /**
     * Sets the MAV amount.
     *
     * The amount is formatted to use a decimal point instead of a comma.
     *
     * @param importoMav The MAV amount entered by the user.
     */
    fun setImportoMavX(importoMav: String) {
        this.importoMav = importoMav.replace(",",".")
    }
    /**
     * Sets the MAV code.
     *
     * @param codiceMav The MAV code entered by the user.
     */
    fun setCodiceMavX(codiceMav: String) {
        this.codiceMav = codiceMav
    }
    /**
     * Sets the MAV sender (ordinante).
     *
     * @param ordinanteMav The MAV sender's account code.
     */
    fun setOrdinanteMavX(ordinanteMav: String) {
        this.ordinanteMav = ordinanteMav
    }
    /**
     * Sets the MAV credit date.
     *
     * @param dataAccredito The new credit date for the MAV operation.
     */
    fun setDataAccreditoMavX(dataAccredito:LocalDateTime) {
      this.dataAccreditoMav = dataAccredito
    }
    /**
     * Validates the MAV fields including the code, amount, and description.
     *
     * This method performs regular expression checks on the MAV code, amount,
     * and description to ensure they meet the required format.
     * Updates the error states accordingly.
     *
     * @return Boolean indicating whether all fields are valid.
     */
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
/**
 * Factory class for creating an instance of `MavViewModel`.
 *
 * This factory is required when the `ViewModel` needs an `Application` instance
 * in its constructor. It is used within `ViewModelProvider`.
 *
 * @property application The application instance passed to the ViewModel.
 */
@Suppress("UNCHECKED_CAST")
class MavViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Creates an instance of `MavViewModel` if the requested class matches.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return The instance of `MavViewModel`.
     * @throws IllegalArgumentException If the requested class is not `MavViewModel`.
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MavViewModel::class.java)) {
            return MavViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}