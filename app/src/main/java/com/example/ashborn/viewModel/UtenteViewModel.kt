package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.OfflineUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
/**
 * UtenteViewModel is a ViewModel responsible for managing user data, including client code,
 * user information, and interacting with local and persistent data storage.
 *
 * This ViewModel uses both the `DataStoreManager` for managing user preferences and a
 * repository pattern through `OfflineUserRepository` to retrieve user data from the local database.
 *
 * @property application The application instance used to access the context and data resources.
 */
class UtenteViewModel(application: Application): AndroidViewModel(application) {
    /**
     * The name of the current class. Used in logs to identify operations performed by this class.
     */
    private val nameClass = ContiViewModel::class.simpleName
    /**
     * The name of the current class. Used in logs to identify operations performed by this class.
     */
    private val dsm = DataStoreManager.getInstance(application)

    /**
     * Data Access Object (DAO) for interacting with the Ashborn database, used to access user data.
     */
    private val ashbornDao: AshbornDao = AshbornDb.getDatabase(application).ashbornDao()
    /**
     * Repository for managing offline user data. Provides an abstraction layer between
     * the ViewModel and the database.
     */
    private val userRepository: OfflineUserRepository = OfflineUserRepository(ashbornDao)
    /**
     * Client code (codCliente) retrieved from the DataStore.
     * The value is fetched synchronously using a blocking coroutine on the IO dispatcher.
     */
    val codCliente = runBlocking {

        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }
    /**
     * User object corresponding to the client code.
     * This value is retrieved from the local database using the `OfflineUserRepository`.
     *
     * The user data is fetched synchronously using a blocking coroutine on the IO dispatcher.
     */
    var user = runBlocking {
        runBlocking(Dispatchers.IO) {
            userRepository.getUserByClientCode(codCliente).first()
        }
    }
    /**
     * The user's first name, extracted from the `user` object.
     */
    val userName = user.name
    /**
     * The user's last name (surname), extracted from the `user` object.
     */
    val cognome = user.surname
    /**
     * The user's date of birth, extracted from the `user` object.
     */
    val dataNascita = user.dateOfBirth


}
/**
 * Factory class for creating an instance of `UtenteViewModel`.
 *
 * This factory is used when the `ViewModel` requires an `Application` instance
 * in its constructor. It is necessary to instantiate `UtenteViewModel` within
 * the `ViewModelProvider` context.
 *
 * @property application The application instance passed to the ViewModel.
 */
@Suppress("UNCHECKED_CAST")
class UtenteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    /**
     * Creates an instance of `UtenteViewModel` if the requested class is compatible.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return The instance of `UtenteViewModel`.
     * @throws IllegalArgumentException If the requested class is not `UtenteViewModel`.
     */
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UtenteViewModel::class.java)) {
            return UtenteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}