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

class UtenteViewModel(application: Application): AndroidViewModel(application) {

    private val nameClass = ContiViewModel::class.simpleName
    private val dsm = DataStoreManager.getInstance(application)
    private val ashbornDao: AshbornDao = AshbornDb.getDatabase(application).ashbornDao()
    private val userRepository: OfflineUserRepository = OfflineUserRepository(ashbornDao)
    val codCliente = runBlocking {

        var ris = ""
        runBlocking(Dispatchers.IO) {
            dsm.codClienteFlow.first().also { ris = it }
        }
    }
    var user = runBlocking {
        var ris = ""
        runBlocking(Dispatchers.IO) {
            userRepository.getUserByClientCode(codCliente).first()
        }
    }
    val userName = user.name
    val cognome = user.surname
    val dataNascita = user.dateOfBirth


}

@Suppress("UNCHECKED_CAST")
class UtenteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UtenteViewModel::class.java)) {
            return UtenteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}